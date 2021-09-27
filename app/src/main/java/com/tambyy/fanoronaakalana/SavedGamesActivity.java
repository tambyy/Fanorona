package com.tambyy.fanoronaakalana;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tambyy.fanoronaakalana.adapter.SavedGameAdapter;
import com.tambyy.fanoronaakalana.adapter.SavedGameBreadcrumbFolderAdapter;
import com.tambyy.fanoronaakalana.adapter.SavedGameFolderAdapter;
import com.tambyy.fanoronaakalana.database.FanoronaDatabase;
import com.tambyy.fanoronaakalana.engine.Engine;
import com.tambyy.fanoronaakalana.models.Folder;
import com.tambyy.fanoronaakalana.models.Game;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedGamesActivity extends AppCompatActivity {

    @BindView(R.id.saved_games_folders_breadcrumb)
    RecyclerView gridViewSavedGamesFoldersBreadcrumb;

    @BindView(R.id.saved_games_folders_list)
    GridView gridViewSavedGamesFoldersList;

    @BindView(R.id.saved_games_list)
    GridView gridViewSavedGamesList;

    @BindView(R.id.saved_games_folders_list_empty_text)
    TextView textViewSavedGamesFoldersListEmpty;

    @BindView(R.id.saved_games_list_empty_text)
    TextView textViewSavedGamesListEmpty;

    @BindView(R.id.saved_games_options)
    LinearLayout linearLayoutSavedGamesOptions;

    @BindView(R.id.saved_games_remove_items)
    LinearLayout linearLayoutSavedGamesRemoveItems;

    @BindView(R.id.saved_games_move_items)
    LinearLayout linearLayoutSavedGamesMoveItems;

    @BindView(R.id.saved_games_move_items_validate)
    LinearLayout linearLayoutSavedGamesMoveItemsValidate;

    @BindView(R.id.saved_games_cancel)
    LinearLayout linearLayoutSavedGamesCancel;

    @BindView(R.id.saved_games_rename_item)
    LinearLayout linearLayoutSavedGamesRenameItem;

    @BindView(R.id.saved_games_save_game)
    LinearLayout linearLayoutSavedGamesSaveGame;

    @BindView(R.id.saved_games_continue_game)
    LinearLayout linearLayoutSavedGamesContinueGame;

    /**
     * Database
     */
    private FanoronaDatabase database;

    /**
     * Fanorona Engine
     */
    private final Engine engine = new Engine(0);

    /**
     * current folder breadcrumb
     */
    List<Folder> breadcrumb = new ArrayList<>();

    /**
     * Current folder
     */
    Folder currentFolder = null;

    /**
     * List of current folder games
     */
    List<Game> games = new ArrayList<>();

    /**
     * List of current folder subfolders
     */
    List<Folder> folders = new ArrayList<>();

    /**
     * List of current folder games
     */
    List<Game> gamesToMove = new ArrayList<>();

    /**
     * List of current folder subfolders
     */
    List<Folder> foldersToMove = new ArrayList<>();

    SavedGameAdapter savedGameAdapter;
    SavedGameFolderAdapter savedGameFolderAdapter;
    SavedGameBreadcrumbFolderAdapter savedGameBreadcrumbFolderAdapter;

    public static final String GAME_EDIT_NAME_CODE = "GAME_EDIT_NAME_CODE";
    public static final String GAME_EDIT_IMAGE_INDEX_CODE = "GAME_EDIT_IMAGE_INDEX_CODE";
    public static final String GAME_ID_CODE = "GAME_ID_CODE";

    public static final String FOLDER_EDIT_NAME_CODE = "FOLDER_EDIT_NAME_CODE";

    /**
     *
     */
    public static final int GAME_EDIT_CODE = 0;
    public static final int FOLDER_EDIT_CODE = 1;
    public static final int GAME_NEW_CODE = 2;
    public static final int FOLDER_NEW_CODE = 3;

    /**
     * Extras
     */
    public static final String EXTRA_GAME_ID = "EXTRA_GAME_ID";
    public static final String EXTRA_FOLDER_ID = "EXTRA_FOLDER_ID";
    public static final String EXTRA_NEW_GAME_CONFIG = "EXTRA_NEW_GAME_CONFIG";
    public static final String EXTRA_NEW_GAME_IMAGE_INDEX = "EXTRA_NEW_GAME_IMAGE_INDEX";
    public static final String EXTRA_NEW_FOLDER_CONFIG = "EXTRA_NEW_FOLDER_CONFIG";

    private String gameToSaveConfigs = null;
    private int gameToSaveHistoryIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_games);
        ButterKnife.bind(this);

        this.database = FanoronaDatabase.getInstance(this);

        // current folder breadcrumb
        initBreadcrumb();

        // current folder sub folders
        initFoldersList();

        // current folder games
        initGamesList();

        setCurrentFolder(null);

        intentFromGameActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        engine.terminate();
    }

    @Override
    public void onBackPressed() {
        // if there're selected items
        // we unselect items
        if (savedGameAdapter.getSelectedGamesCount() + savedGameFolderAdapter.getSelectedFoldersCount() > 0) {
            savedGameFolderAdapter.clearSelectedFolders();
            savedGameAdapter.clearSelectedGames();
            checkEnabledButtons();
        // else if we are in a subfolder
        // we go back to the parent folder
        } else if (currentFolder != null) {
            setCurrentFolder(getFolder(currentFolder.getParent_id()));
        } else {
        // else we go back to the parent activity
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();

            if (requestCode == GAME_EDIT_CODE) {
                onEditGameActivityResult(bundle);
            } else if (requestCode == FOLDER_EDIT_CODE) {
                onEditFolderActivityResult(bundle);
            } else if (requestCode == GAME_NEW_CODE) {
                onNewGameActivityResult(bundle);
            } else if (requestCode == FOLDER_NEW_CODE) {
                onNewFolderActivityResult(bundle);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // save current folder
        savedInstanceState.putLong(SavedGamesActivity.GAME_ID_CODE, currentFolder != null ? currentFolder.getId() : 0);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        long currentFolderId = savedInstanceState.getLong(SavedGamesActivity.GAME_ID_CODE);
        setCurrentFolder(currentFolderId == 0 ? null : getFolder(currentFolderId));
    }

    /**
     *
     */
    public void intentFromGameActivity() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            if (bundle.containsKey(OptionActivity.EXTRA_GAME_RESUME)) {
                gameToSaveConfigs = bundle.getString(GameActivity.EXTRA_GAME_RESUME);
                gameToSaveHistoryIndex = bundle.getInt(GameActivity.EXTRA_GAME_RESUME_HISTORY_INDEX);
                checkEnabledButtons();
            }
        }
    }

    /**
     *
     */
    public void launchSavedGamesReviewActivity(Game game) {
        Intent intent = new Intent(this, SavedGamesReviewActivity.class);

        intent.putExtra(EXTRA_GAME_ID, game.getId());
        this.startActivity(intent);
    }

    /**
     *
     */
    public void launchOptionActivity(View view) {
        if (savedGameAdapter.getSelectedGamesCount() == 1 && savedGameFolderAdapter.getSelectedFoldersCount() == 0) {
            Intent intent = new Intent(this, OptionActivity.class);

            intent.putExtra(OptionActivity.SAVED_GAMES_CONFIG_CODE, savedGameAdapter.getSelectedGames().get(0).getConfigs());
            this.startActivity(intent);
        }
    }

    /**
     *
     */
    private void initBreadcrumb() {
        savedGameBreadcrumbFolderAdapter = new SavedGameBreadcrumbFolderAdapter(this, breadcrumb);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        gridViewSavedGamesFoldersBreadcrumb.setAdapter(savedGameBreadcrumbFolderAdapter);
        gridViewSavedGamesFoldersBreadcrumb.setLayoutManager(layoutManager);
    }

    /**
     *
     */
    private void initGamesList() {
        savedGameAdapter = new SavedGameAdapter(this, games, engine);
        gridViewSavedGamesList.setAdapter(savedGameAdapter);
        gridViewSavedGamesList.setOnItemLongClickListener((parent, view, position, id) -> {
            if (savedGameAdapter.getSelectedGamesCount() + savedGameFolderAdapter.getSelectedFoldersCount() == 0) {
                movingSelectedItemsCancel();
                savedGameAdapter.toggleSelectGame(games.get(position));
                checkEnabledButtons();
            }
            return true;
        });
        gridViewSavedGamesList.setOnItemClickListener((parent, view, position, id) -> {
            if (savedGameAdapter.getSelectedGamesCount() + savedGameFolderAdapter.getSelectedFoldersCount() == 0) {
                launchSavedGamesReviewActivity(games.get(position));
            } else {
                savedGameAdapter.toggleSelectGame(games.get(position));
                checkEnabledButtons();
            }
        });
    }

    /**
     *
     */
    private void initFoldersList() {
        savedGameFolderAdapter = new SavedGameFolderAdapter(this, folders);
        gridViewSavedGamesFoldersList.setAdapter(savedGameFolderAdapter);
        gridViewSavedGamesFoldersList.setOnItemLongClickListener((parent, view, position, id) -> {
            if (savedGameAdapter.getSelectedGamesCount() + savedGameFolderAdapter.getSelectedFoldersCount() == 0) {
                movingSelectedItemsCancel();
                savedGameFolderAdapter.toggleSelectFolder(folders.get(position));
                checkEnabledButtons();
            }
            return true;
        });
        gridViewSavedGamesFoldersList.setOnItemClickListener((parent, view, position, id) -> {
            if (savedGameAdapter.getSelectedGamesCount() + savedGameFolderAdapter.getSelectedFoldersCount() == 0) {
                setCurrentFolder(folders.get(position));
            } else {
                savedGameFolderAdapter.toggleSelectFolder(folders.get(position));
                checkEnabledButtons();
            }
        });
    }

    /**
     * Retrieve the folder
     * having the id folderId
     * in database
     *
     * @param folderId
     * @return
     */
    private Folder getFolder(Long folderId) {
        if (folderId == null) {
            return null;
        } else {
            return this.database.folderDao().getFolder(folderId);
        }
    }

    /**
     * Show a dialog box alowing us to create
     * a new folder inside the current folder
     * @param view
     */
    public void createFolder(View view) {
        Intent intent = new Intent(this, SavedGamesEditFolderActivity.class);
        startActivityForResult(intent, FOLDER_NEW_CODE);
    }

    /**
     * After validating a folder création
     * we retrieve the given folder name
     * and create the folder in database
     * inside the durrent folder
     *
     * @param name
     *        name of the folder to create
     */
    private void storeFolder(String name) {
        Folder folder = new Folder();
        folder.setName(name);
        folder.setCreated_at(new Date());
        folder.setParent_id(currentFolder != null ? currentFolder.getId() : null);

        folder.setId(this.database.folderDao().insertFolder(folder));
        this.folders.add(folder);

        this.refreshCurrentFolder();
    }

    /**
     *
     * @param folder
     */
    private void updateFolder(Folder folder) {
        this.database.folderDao().updateFolder(folder);

        this.refreshCurrentFolder();
    }

    /**
     * Remove a given folder from database
     * @param folder
     */
    private void removeFolder(Folder folder) {
        this.folders.remove(folder);
        this.database.folderDao().deleteFolder(folder.getId());
    }

    /**
     * Show a dialog box aloowing us to create
     * a new folder inside the current folder
     * @param view
     */
    public void createGame(View view) {
        Intent intent = new Intent(this, SavedGamesEditGameActivity.class);
        intent.putExtra(EXTRA_NEW_GAME_CONFIG, gameToSaveConfigs);
        intent.putExtra(EXTRA_NEW_GAME_IMAGE_INDEX, gameToSaveHistoryIndex);
        startActivityForResult(intent, GAME_NEW_CODE);
    }

    /**
     *
     * @param name
     */
    private void storeGame(String name, int imageIndex) {
        if (gameToSaveConfigs != null) {
            Game game = new Game();
            game.setName(name);
            game.setImage_index(imageIndex);
            game.setConfigs(gameToSaveConfigs);
            game.setCreated_at(new Date());
            game.setFolder_id(currentFolder != null ? currentFolder.getId() : null);

            game.setId(this.database.gameDao().insertGame(game));
            games.add(0, game);

            gameToSaveConfigs = null;
            gameToSaveHistoryIndex = 0;

            this.refreshCurrentFolder();
        }
    }

    /**
     *
     * @param view
     */
    public void editGame(View view) {
        if (savedGameAdapter.getSelectedGamesCount() == 1 && savedGameFolderAdapter.getSelectedFoldersCount() == 0) {
            Game game = savedGameAdapter.getSelectedGames().get(0);
            launchEditGameActivity(game);
        } else if (savedGameFolderAdapter.getSelectedFoldersCount() == 1 && savedGameAdapter.getSelectedGamesCount() == 0) {
            Folder folder = savedGameFolderAdapter.getSelectedFolders().get(0);
            launchEditFolderActivity(folder);
        }
    }

    /**
     *
     * @param game
     */
    private void updateGame(Game game) {
        this.database.gameDao().updateGame(game);

        this.refreshCurrentFolder();
    }

    /**
     * Remove a given game from database
     * @param game
     */
    private void removeGame(Game game) {
        this.games.remove(game);
        this.database.gameDao().deleteGame(game.getId());
    }

    /**
     *
     * @param view
     */
    public void removeSelectedItems(View view) {

        // remove selected folders
        for (Folder folder: savedGameFolderAdapter.getSelectedFolders()) {
            removeFolder(folder);
        }

        // remove selected games
        for (Game game: savedGameAdapter.getSelectedGames()) {
            removeGame(game);
        }

        this.refreshCurrentFolder();
    }

    /**
     *
     * @param view
     */
    public void movingSelectedItems(View view) {
        gamesToMove.addAll(savedGameAdapter.getSelectedGames());
        foldersToMove.addAll(savedGameFolderAdapter.getSelectedFolders());

        savedGameFolderAdapter.clearSelectedFolders();
        savedGameAdapter.clearSelectedGames();
        checkEnabledButtons();
    }

    /**
     *
     * @param view
     */
    public void moveSelectedItems(View view) {
        Long parentId = currentFolder != null ? currentFolder.getId() : null;

        List<Folder> parentFolders = getParentFolders(currentFolder);

        // remove selected folders
        for (Folder folder: foldersToMove) {
            boolean skip = false;

            // avoid moving a folder into the same folder
            for (Folder parentFolder: parentFolders) {
                if (parentFolder.getId() == folder.getId()) {
                    skip = true;
                    break;
                }
            }

            if (!skip) {
                folder.setParent_id(parentId);
                database.folderDao().updateFolder(folder);
            }
        }

        // remove selected games
        for (Game game: gamesToMove) {
            game.setFolder_id(parentId);
            database.gameDao().updateGame(game);
        }

        foldersToMove.clear();
        gamesToMove.clear();
        this.refreshCurrentFolder();
    }

    /**
     *
     */
    public void movingSelectedItemsCancel() {
        foldersToMove.clear();
        gamesToMove.clear();
        checkEnabledButtons();
    }

    /**
     *
     * @param view
     */
    public void movingSelectedItemsCancel(View view) {
        movingSelectedItemsCancel();
    }

    /**
     *
     * @param children
     * @return
     */
    private List<Folder> getParentFolders(Folder children) {
        List<Folder> folders = new ArrayList<>();

        // from the current folder
        // to the root folder
        // we put a folder at the beginning of the breadcrumb
        for (Folder folder = children; folder != null; folder = getFolder(folder.getParent_id())) {
            folders.add(0, folder);
        }

        return folders;
    }

    /**
     * Set folder
     * as the current folder
     *
     * @param folder
     */
    public void setCurrentFolder(Folder folder) {
        this.currentFolder = folder;

        this.folders.clear();
        this.games.clear();

        // we retrieve folders and games
        // inside the current folder

        // for root folder
        if (folder == null) {
            this.folders.addAll(this.database.folderDao().getRootFolders());
            this.games.addAll(this.database.gameDao().getRootGames());
            // for sub folder
        } else {
            this.folders.addAll(this.database.folderDao().getChildFolders(folder.getId()));
            this.games.addAll(this.database.gameDao().getChildGames(folder.getId()));
        }

        // update folders and list view
        savedGameFolderAdapter.clearSelectedFolders();
        savedGameAdapter.clearSelectedGames();

        if (this.folders.isEmpty()) {
            this.gridViewSavedGamesFoldersList.setVisibility(View.GONE);
            this.textViewSavedGamesFoldersListEmpty.setVisibility(View.VISIBLE);
        } else {
            this.gridViewSavedGamesFoldersList.setVisibility(View.VISIBLE);
            this.textViewSavedGamesFoldersListEmpty.setVisibility(View.GONE);
        }

        if (this.games.isEmpty()) {
            this.gridViewSavedGamesList.setVisibility(View.GONE);
            this.textViewSavedGamesListEmpty.setVisibility(View.VISIBLE);
        } else {
            this.gridViewSavedGamesList.setVisibility(View.VISIBLE);
            this.textViewSavedGamesListEmpty.setVisibility(View.GONE);
        }

        // update breadcrumb
        updateBreadCrumb();

        toggleGamesFoldersTitle();
        checkEnabledButtons();
    }

    /**
     *
     */
    private void refreshCurrentFolder() {
        setCurrentFolder(currentFolder);
    }

    /**
     * Update folders breadcrumb
     */
    private void updateBreadCrumb() {
        breadcrumb.clear();

        breadcrumb.addAll(getParentFolders(currentFolder));

        // add root folder
        breadcrumb.add(0, null);

        // update breadcrumb view
        savedGameBreadcrumbFolderAdapter.notifyDataSetChanged();
    }

    /**
     *
     * @param game
     */
    private void launchEditGameActivity(Game game) {
        Intent intent = new Intent(this, SavedGamesEditGameActivity.class);
        intent.putExtra(EXTRA_GAME_ID, game.getId());
        startActivityForResult(intent, GAME_EDIT_CODE);
    }

    /**
     *
     * @param folder
     */
    private void launchEditFolderActivity(Folder folder) {
        Intent intent = new Intent(this, SavedGamesEditFolderActivity.class);
        intent.putExtra(EXTRA_FOLDER_ID, folder.getId());
        startActivityForResult(intent, FOLDER_EDIT_CODE);
    }

    /**
     *
     * @param bundle
     */
    private void onEditGameActivityResult(Bundle bundle) {
        if (savedGameAdapter.getSelectedGamesCount() == 1) {
            Game game = savedGameAdapter.getSelectedGames().get(0);

            game.setName(bundle.getString(GAME_EDIT_NAME_CODE));
            game.setImage_index(bundle.getInt(GAME_EDIT_IMAGE_INDEX_CODE));

            updateGame(game);
        }
    }

    /**
     *
     * @param bundle
     */
    private void onNewGameActivityResult(Bundle bundle) {
        String gameName = bundle.getString(GAME_EDIT_NAME_CODE);
        int imageIndex = bundle.getInt(GAME_EDIT_IMAGE_INDEX_CODE);
        storeGame(gameName, imageIndex);
    }

    /**
     *
     * @param bundle
     */
    private void onEditFolderActivityResult(Bundle bundle) {
        if (savedGameFolderAdapter.getSelectedFoldersCount() == 1) {
            Folder folder = savedGameFolderAdapter.getSelectedFolders().get(0);

            folder.setName(bundle.getString(FOLDER_EDIT_NAME_CODE));

            updateFolder(folder);
        }
    }

    /**
     *
     * @param bundle
     */
    private void onNewFolderActivityResult(Bundle bundle) {
        String folderName = bundle.getString(FOLDER_EDIT_NAME_CODE);
        storeFolder(folderName);
    }

    /**
     *
     */
    private void toggleGamesFoldersTitle() {/*
        if (games.size() > 0) {
            textViewSavedGamesListTitle.setVisibility(View.VISIBLE);
            textViewSavedGamesListTitle.setText(games.size() + " parties sauvegardées");
            gridViewSavedGamesList.setVisibility(View.VISIBLE);
        } else {
            textViewSavedGamesListTitle.setVisibility(View.GONE);
            gridViewSavedGamesList.setVisibility(View.GONE);
        }

        if (folders.size() > 0) {
            textViewSavedGamesFoldersListTitle.setVisibility(View.VISIBLE);
            textViewSavedGamesFoldersListTitle.setText(folders.size() + " dossiers");
            gridViewSavedGamesFoldersList.setVisibility(View.VISIBLE);
        } else {
            textViewSavedGamesFoldersListTitle.setVisibility(View.GONE);
            gridViewSavedGamesFoldersList.setVisibility(View.GONE);
        }*/
    }

    /**
     *
     */
    private void checkEnabledButtons() {
        int selectedItemsCount = savedGameAdapter.getSelectedGamesCount() + savedGameFolderAdapter.getSelectedFoldersCount();

        linearLayoutSavedGamesRenameItem.setVisibility(selectedItemsCount == 1 ? View.VISIBLE : View.GONE);
        linearLayoutSavedGamesRemoveItems.setVisibility(selectedItemsCount > 0 ? View.VISIBLE : View.GONE);
        linearLayoutSavedGamesMoveItems.setVisibility(selectedItemsCount > 0 ? View.VISIBLE : View.GONE);
        linearLayoutSavedGamesMoveItemsValidate.setVisibility(gamesToMove.size() > 0 || foldersToMove.size() > 0 ? View.VISIBLE : View.GONE);
        linearLayoutSavedGamesCancel.setVisibility(gamesToMove.size() > 0 || foldersToMove.size() > 0 ? View.VISIBLE : View.GONE);
        linearLayoutSavedGamesSaveGame.setVisibility(gameToSaveConfigs != null ? View.VISIBLE : View.GONE);
        linearLayoutSavedGamesContinueGame.setVisibility(selectedItemsCount == 1 && savedGameFolderAdapter.getSelectedFoldersCount() == 0 ? View.VISIBLE : View.GONE);
    }
}