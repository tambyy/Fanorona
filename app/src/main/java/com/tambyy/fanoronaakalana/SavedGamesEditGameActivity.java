package com.tambyy.fanoronaakalana;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.tambyy.fanoronaakalana.adapter.EditGameImageAdapter;
import com.tambyy.fanoronaakalana.database.FanoronaDatabase;
import com.tambyy.fanoronaakalana.engine.Engine;
import com.tambyy.fanoronaakalana.engine.EngineAction;
import com.tambyy.fanoronaakalana.models.Game;
import com.tambyy.fanoronaakalana.utils.EngineActionsConverter;

public class SavedGamesEditGameActivity extends AppCompatActivity {

    @BindView(R.id.edit_game_images_list)
    GridView gridViewEditGameImagesList;

    @BindView(R.id.edit_game_name)
    EditText editTextEditGameName;

    /**
     * Database
     */
    private FanoronaDatabase database;

    /**
     *
     */
    private Game game;

    /**
     *
     */
    private Engine engine = new Engine(0);

    EditGameImageAdapter editGameImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_games_edit_game);
        ButterKnife.bind(this);

        setPopupDisplay();

        this.database = FanoronaDatabase.getInstance(this);

        intentFromSavedGamesActivity();

        editTextEditGameName.requestFocus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        engine.terminate();
    }

    private void setPopupDisplay() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.7), (int) (height * 0.9));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;

        getWindow().setAttributes(params);
    }

    private void intentFromSavedGamesActivity() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            if (bundle.containsKey(SavedGamesActivity.EXTRA_GAME_ID)) {
                long gameId = bundle.getLong(SavedGamesActivity.EXTRA_GAME_ID);

                this.game = this.database.gameDao().getGame(gameId);

                if (this.game != null) {
                    editTextEditGameName.setText(this.game.getName());
                    initImagesList();
                }
            } else if (bundle.containsKey(SavedGamesActivity.EXTRA_NEW_GAME_CONFIG)) {
                this.game = new Game();
                this.game.setName("");
                this.game.setConfigs(bundle.getString(SavedGamesActivity.EXTRA_NEW_GAME_CONFIG));
                this.game.setImage_index(bundle.getInt(SavedGamesActivity.EXTRA_NEW_GAME_IMAGE_INDEX));
                initImagesList();
            }
        }
    }

    /**
     *
     */
    private void initImagesList() {
        editGameImageAdapter = new EditGameImageAdapter(this, EngineActionsConverter.stringToEngineActions(this.game.getConfigs()), engine);
        gridViewEditGameImagesList.setAdapter(editGameImageAdapter);
        gridViewEditGameImagesList.setOnItemClickListener((parent, view, position, id) -> {
            editGameImageAdapter.setSelectedImage(position);
        });
        editGameImageAdapter.setSelectedImage(editGameImageAdapter.getActionsToShow().indexOf(editGameImageAdapter.getActions().get(this.game.getImage_index())));
    }

    /**
     *
     * @param view
     */
    public void validateEdition(View view) {
        Intent intent = new Intent();

        String gameName = editTextEditGameName.getText().toString();
        if (gameName.isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.saved_games_game_name_empty_msg), Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedImage = editGameImageAdapter.getSelectedImage();
        EngineAction action = editGameImageAdapter.getActionsToShow().get(selectedImage);

        intent.putExtra(SavedGamesActivity.GAME_EDIT_NAME_CODE, gameName);
        intent.putExtra(SavedGamesActivity.GAME_EDIT_IMAGE_INDEX_CODE, editGameImageAdapter.getActions().indexOf(action));
        setResult(RESULT_OK, intent);

        finish();
    }
}