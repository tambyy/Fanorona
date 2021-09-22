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

import com.tambyy.fanoronaakalana.database.FanoronaDatabase;
import com.tambyy.fanoronaakalana.engine.EngineAction;
import com.tambyy.fanoronaakalana.models.Folder;
import com.tambyy.fanoronaakalana.models.Game;

public class SavedGamesEditFolderActivity extends AppCompatActivity {

    @BindView(R.id.edit_folder_name)
    EditText editTextEditFolderName;

    /**
     * Database
     */
    private FanoronaDatabase database;

    /**
     *
     */
    private Folder folder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_games_edit_folder);
        ButterKnife.bind(this);

        setPopupDisplay();

        this.database = FanoronaDatabase.getInstance(this);

        intentFromSavedGamesActivity();

        editTextEditFolderName.requestFocus();
    }

    private void setPopupDisplay() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.4), WindowManager.LayoutParams.WRAP_CONTENT);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;

        getWindow().setAttributes(params);
    }

    private void intentFromSavedGamesActivity() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            if (bundle.containsKey(SavedGamesActivity.EXTRA_FOLDER_ID)) {
                long folderId = bundle.getLong(SavedGamesActivity.EXTRA_FOLDER_ID);

                this.folder = this.database.folderDao().getFolder(folderId);

                if (this.folder != null) {
                    editTextEditFolderName.setText(this.folder.getName());
                }
            }
        }
    }

    /**
     *
     * @param view
     */
    public void validateEdition(View view) {
        Intent intent = new Intent();

        intent.putExtra(SavedGamesActivity.FOLDER_EDIT_NAME_CODE, editTextEditFolderName.getText().toString());
        setResult(RESULT_OK, intent);

        finish();
    }
}