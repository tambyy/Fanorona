package com.tambyy.fanoronaakalana;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tambyy.fanoronaakalana.adapter.SavedGameAdapter;
import com.tambyy.fanoronaakalana.adapter.SavedGameBreadcrumbFolderAdapter;
import com.tambyy.fanoronaakalana.adapter.SavedGameFolderAdapter;
import com.tambyy.fanoronaakalana.bluetooth.BluetoothService;
import com.tambyy.fanoronaakalana.database.FanoronaDatabase;
import com.tambyy.fanoronaakalana.engine.Engine;
import com.tambyy.fanoronaakalana.models.Folder;
import com.tambyy.fanoronaakalana.models.Game;
import com.tambyy.fanoronaakalana.utils.GameFolderToJsonConverter;
import com.tambyy.fanoronaakalana.utils.PreferenceManager;
import com.tambyy.fanoronaakalana.utils.ThemeManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    RecyclerView recyvlerViewSavedGamesFoldersBreadcrumb;

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

    @BindView(R.id.saved_games_send_bt)
    LinearLayout linearLayoutSavedGamesSendBt;

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

    @BindView(R.id.saved_games_bt_send)
    ImageButton imageButtonSavedGamesBtSend;

    @BindView(R.id.saved_games_bt_receive)
    ImageButton imageButtonSavedGamesBtReceive;

    /**
     * Database
     */
    private FanoronaDatabase database;

    /**
     * Preference
     */
    private PreferenceManager preferenceManager;

    /**
     * Theme
     */
    private ThemeManager themeManager;

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

    /**
     * Member fields
     */
    private BluetoothAdapter bluetoothAdapter;

    /**
     * Member object for the chat services
     */
    private BluetoothService mChatService = null;

    GameFolderToJsonConverter gameFolderToJsonConverter;

    public static final String GAME_EDIT_NAME_CODE = "GAME_EDIT_NAME_CODE";
    public static final String GAME_EDIT_IMAGE_INDEX_CODE = "GAME_EDIT_IMAGE_INDEX_CODE";
    public static final String GAME_FOLDER_ID_CODE = "GAME_FOLDER_ID_CODE";

    public static final String FOLDER_EDIT_NAME_CODE = "FOLDER_EDIT_NAME_CODE";

    /**
     *
     */
    public static final int GAME_EDIT_CODE = 0;
    public static final int FOLDER_EDIT_CODE = 1;
    public static final int GAME_NEW_CODE = 2;
    public static final int FOLDER_NEW_CODE = 3;
    private static final int BLUETOOTH_DEVICE_CODE = 4;
    private static final int REQUEST_ENABLE_BT_SEND_CODE = 5;
    private static final int REQUEST_ENABLE_BT_RECEIVE_CODE = 6;

    /**
     * Extras
     */
    public static final String EXTRA_GAME_ID = "EXTRA_GAME_ID";
    public static final String EXTRA_FOLDER_ID = "EXTRA_FOLDER_ID";
    public static final String EXTRA_NEW_GAME_CONFIG = "EXTRA_NEW_GAME_CONFIG";
    public static final String EXTRA_NEW_GAME_IMAGE_INDEX = "EXTRA_NEW_GAME_IMAGE_INDEX";
    public static final String EXTRA_NEW_FOLDER_CONFIG = "EXTRA_NEW_FOLDER_CONFIG";
    public static final String EXTRA_DEVICE_ADDRESS = "EXTRA_DEVICE_ADDRESS";

    private String gameToSaveConfigs = null;
    private int gameToSaveHistoryIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_games);
        ButterKnife.bind(this);

        this.database = FanoronaDatabase.getInstance(this);
        this.preferenceManager = PreferenceManager.getInstance(this);
        this.themeManager = ThemeManager.getInstance(this);
        this.gameFolderToJsonConverter = GameFolderToJsonConverter.getInstance(this);

        // current folder breadcrumb
        initBreadcrumb();

        // current folder sub folders
        initFoldersList();

        // current folder games
        initGamesList();

        setCurrentFolder(null);

        intentFromGameActivity();

        // loadPreferences();

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            imageButtonSavedGamesBtSend.setVisibility(View.GONE);
            imageButtonSavedGamesBtReceive.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        engine.terminate();

        if (mChatService != null) {
            mChatService.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothService.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();
            }
        }
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
            if (requestCode == REQUEST_ENABLE_BT_SEND_CODE) {
                onRequestEnableBtSendResult();
                return;
            } else if (requestCode == REQUEST_ENABLE_BT_RECEIVE_CODE) {
                onRequestEnableBtReceiveResult();
                return;
            }

            Bundle bundle = data.getExtras();

            if (requestCode == GAME_EDIT_CODE) {
                onEditGameActivityResult(bundle);
            } else if (requestCode == FOLDER_EDIT_CODE) {
                onEditFolderActivityResult(bundle);
            } else if (requestCode == GAME_NEW_CODE) {
                onNewGameActivityResult(bundle);
            } else if (requestCode == FOLDER_NEW_CODE) {
                onNewFolderActivityResult(bundle);
            } else if (requestCode == BLUETOOTH_DEVICE_CODE) {
                onBluetoothDevicesListActivityResult(bundle);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // save current folder
        savedInstanceState.putLong(SavedGamesActivity.GAME_FOLDER_ID_CODE, currentFolder != null ? currentFolder.getId() : 0);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        long currentFolderId = savedInstanceState.getLong(SavedGamesActivity.GAME_FOLDER_ID_CODE);
        //setCurrentFolder(currentFolderId == 0 ? null : getFolder(currentFolderId));
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
    public void launchBluetoothDevicesListActivity() {
        Intent intent = new Intent(this, BluetoothDeviceListActivity.class);
        startActivityForResult(intent, BLUETOOTH_DEVICE_CODE);
    }

    /**
     *
     */
    private void initBreadcrumb() {
        savedGameBreadcrumbFolderAdapter = new SavedGameBreadcrumbFolderAdapter(this, breadcrumb);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyvlerViewSavedGamesFoldersBreadcrumb.setAdapter(savedGameBreadcrumbFolderAdapter);
        recyvlerViewSavedGamesFoldersBreadcrumb.setLayoutManager(layoutManager);
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
    private Folder storeFolder(String name, Folder parent) {
        Folder folder = new Folder();
        folder.setName(name);
        folder.setCreated_at(new Date());
        folder.setParent_id(parent != null ? parent.getId() : null);

        folder.setId(this.database.folderDao().insertFolder(folder));

        return folder;
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
    private Game storeGame(String name, String config, int imageIndex, Folder folder) {
        Game game = new Game();
        game.setName(name);
        game.setImage_index(imageIndex);
        game.setConfigs(config);
        game.setCreated_at(new Date());
        game.setFolder_id(folder != null ? folder.getId() : null);

        game.setId(this.database.gameDao().insertGame(game));

        return game;
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

        games.add(0, storeGame(gameName, gameToSaveConfigs, imageIndex, currentFolder));

        gameToSaveConfigs = null;
        gameToSaveHistoryIndex = 0;

        this.refreshCurrentFolder();
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
    private void onBluetoothDevicesListActivityResult(Bundle bundle) {
        String deviceAddress = bundle.getString(EXTRA_DEVICE_ADDRESS);

        if (mChatService == null) {
            bluetoothServiceSetup();
        } else {
            mChatService.stop();
        }

        if (mChatService.getState() != BluetoothService.STATE_CONNECTED) {
            // Get the BluetoothDevice object
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
            // Attempt to connect to the device
            mChatService.connect(device, true);
            checkEnabledButtons();
        }
    }

    /**
     *
     * @param bundle
     */
    private void onNewFolderActivityResult(Bundle bundle) {
        String folderName = bundle.getString(FOLDER_EDIT_NAME_CODE);

        this.folders.add(storeFolder(folderName, currentFolder));
        this.refreshCurrentFolder();
    }

    /**
     *
     */
    private void onRequestEnableBtSendResult() {
        // Bluetooth is now enabled
        launchBluetoothDevicesListActivity();
    }

    /**
     *
     */
    private void onRequestEnableBtReceiveResult() {
        bluetoothServiceSetup();
        mChatService.start();
        imageButtonSavedGamesBtReceive.setImageResource(R.drawable.saved_game_bt_receive_active_ic);
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
        boolean bluetoothConnected = mChatService != null && mChatService.getState() == BluetoothService.STATE_CONNECTED;

        linearLayoutSavedGamesSendBt.setVisibility(selectedItemsCount > 0 && bluetoothConnected ? View.VISIBLE : View.GONE);
        linearLayoutSavedGamesRenameItem.setVisibility(selectedItemsCount == 1 ? View.VISIBLE : View.GONE);
        linearLayoutSavedGamesRemoveItems.setVisibility(selectedItemsCount > 0 ? View.VISIBLE : View.GONE);
        linearLayoutSavedGamesMoveItems.setVisibility(selectedItemsCount > 0 ? View.VISIBLE : View.GONE);
        linearLayoutSavedGamesMoveItemsValidate.setVisibility(gamesToMove.size() > 0 || foldersToMove.size() > 0 ? View.VISIBLE : View.GONE);
        linearLayoutSavedGamesCancel.setVisibility(gamesToMove.size() > 0 || foldersToMove.size() > 0 ? View.VISIBLE : View.GONE);
        linearLayoutSavedGamesSaveGame.setVisibility(gameToSaveConfigs != null ? View.VISIBLE : View.GONE);
        linearLayoutSavedGamesContinueGame.setVisibility(selectedItemsCount == 1 && savedGameFolderAdapter.getSelectedFoldersCount() == 0 ? View.VISIBLE : View.GONE);

        savedGameAdapter.setShowSelectionBox(selectedItemsCount > 0);
        savedGameFolderAdapter.setShowSelectionBox(selectedItemsCount > 0);

        imageButtonSavedGamesBtSend.setImageResource(bluetoothConnected ? R.drawable.saved_game_bt_send_active_ic : R.drawable.saved_game_bt_send_ic);
    }

    /**
     *
     */
    private void loadPreferences() {

        // Theme

        themeManager.getTheme(preferenceManager.get(ThemeManager.PREF_THEME, 1l), theme -> {
            if (theme != null) {
                savedGameAdapter.setTheme(theme);
            }
        });
    }


    // BLuetooth

    public void bluetoothSelectDevice(View view) {
        // Enable bluetooth request
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT_SEND_CODE);
        } else if (mChatService == null || mChatService.getState() == BluetoothService.STATE_NONE) {
            launchBluetoothDevicesListActivity();
        } else {
            mChatService.stop();
            mChatService = null;
            checkEnabledButtons();
        }
    }

    /**
     * Set up the UI and background operations for chat.
     */
    private void bluetoothServiceSetup() {
        if (mChatService == null) {
            // Initialize the BluetoothChatService to perform bluetooth connections
            mChatService = new BluetoothService(this, bluetoothHandler);
        }
    }

    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    private final Handler bluetoothHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                        case BluetoothService.STATE_CONNECTING:
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            checkEnabledButtons();
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);

                    SavedGamesActivity.this.bluetoothSaveData(readMessage);
                    break;
            }
        }
    };

    /**
     * Sends a message.
     *
     */
    public void bluetoothSendData(View view) {
        // Check that we're actually connected before trying anything
        if (mChatService == null || mChatService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(this, "Non connecté", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        String dataToSend = bluetoothGetDataToSend();
        if (dataToSend != null) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = dataToSend.getBytes();
            mChatService.write(send);
            savedGameAdapter.clearSelectedGames();
            savedGameFolderAdapter.clearSelectedFolders();
            checkEnabledButtons();
        }
    }

    /**
     * Sends a message.
     *
     */
    private String bluetoothGetDataToSend() {
        JSONArray jsonArray = gameFolderToJsonConverter.convert(savedGameFolderAdapter.getSelectedFolders(), savedGameAdapter.getSelectedGames());
        return jsonArray.toString() + " ";
    }

    /**
     * Makes this device discoverable for 300 seconds (5 minutes).
     */
    private void bluetoothEnsureDiscoverable() {
        if (bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    /**
     * Makes this device discoverable for 300 seconds (5 minutes).
     */
    public void bluetoothPrepareReceivingData(View view) {
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT_RECEIVE_CODE);
        } else if (mChatService == null || mChatService.getState() == BluetoothService.STATE_NONE) {
            bluetoothServiceSetup();
            mChatService.start();
            imageButtonSavedGamesBtReceive.setImageResource(R.drawable.saved_game_bt_receive_active_ic);
        } else {
            mChatService.stop();
            mChatService = null;
            imageButtonSavedGamesBtReceive.setImageResource(R.drawable.saved_game_bt_receive_ic);
        }
    }

    /**
     * Makes this device discoverable for 300 seconds (5 minutes).
     */
    private void bluetoothSaveData(String data) {
        this.runOnUiThread(() -> Toast.makeText(this, R.string.saved_games_transfert_runnning, Toast.LENGTH_SHORT));

        try {
            JSONArray jsonArray = new JSONArray(data);
            bluetoothSaveGameFolder(jsonArray, currentFolder);
            this.refreshCurrentFolder();
            this.runOnUiThread(() -> Toast.makeText(this, R.string.saved_games_transfert_finished, Toast.LENGTH_SHORT));
        } catch (JSONException e) {
            Log.e("AKALANA", "bluetoothSaveData", e);
        }
    }

    /**
     */
    private void bluetoothSaveGameFolder(JSONArray jsonArray, Folder folder) {
        for (int i = 0, length = jsonArray.length(); i < length; ++i) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String type = jsonObject.getString("type");
                if (type.equals("game")) {
                    bluetoothSaveGame(jsonObject, folder);
                } else if (type.equals("folder")) {
                    bluetoothSaveFolder(jsonObject, folder);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     */
    private void bluetoothSaveGame(JSONObject jsonObject, Folder folder) {
        try {
            storeGame(
                    jsonObject.getString("name"),
                    jsonObject.getString("config"),
                    jsonObject.getInt("image_index"),
                    folder
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     */
    private void bluetoothSaveFolder(JSONObject jsonObject, Folder parent) {
        try {
            Folder folder = storeFolder(
                    jsonObject.getString("name"),
                    parent
            );
            bluetoothSaveGameFolder(jsonObject.getJSONArray("child"), folder);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}