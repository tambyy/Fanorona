package com.tambyy.fanoronaakalana.database;

import android.content.ContentValues;
import android.content.Context;

import com.tambyy.fanoronaakalana.dao.FolderDao;
import com.tambyy.fanoronaakalana.dao.GameDao;
import com.tambyy.fanoronaakalana.dao.PreferenceDao;
import com.tambyy.fanoronaakalana.dao.ThemeDao;
import com.tambyy.fanoronaakalana.models.Folder;
import com.tambyy.fanoronaakalana.models.Game;
import com.tambyy.fanoronaakalana.models.Preference;
import com.tambyy.fanoronaakalana.models.Theme;
import com.tambyy.fanoronaakalana.utils.DateTimestampConverter;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Folder.class, Game.class, Preference.class, Theme.class}, version = 1, exportSchema = false)
@TypeConverters({DateTimestampConverter.class})
public abstract class FanoronaDatabase extends RoomDatabase {

    // --- SINGLETON ---
    private static volatile FanoronaDatabase INSTANCE;

    // --- DAO ---
    public abstract FolderDao folderDao();
    public abstract GameDao gameDao();
    public abstract PreferenceDao preferenceDao();
    public abstract ThemeDao themeDao();

    // --- INSTANCE ---
    public static FanoronaDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (FanoronaDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FanoronaDatabase.class, "FanoronaDatabase.db")
                            .allowMainThreadQueries()
                            .addCallback(prepopulateDatabase())
                            .build();
                }
            }
        }
        return INSTANCE;
    }


    // ---

    private static Callback prepopulateDatabase(){
        return new Callback() {

            @Override
            public void onOpen(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                // Folder

                prepopulateDatabaseWithFolders(db);
                prepopulateDatabaseWithGames(db);
                prepopulateDatabaseWithThemes(db);
            }
        };
    }

    private static void prepopulateDatabaseWithFolders(SupportSQLiteDatabase db) {
        long dateNow = new Date().getTime();

        ContentValues contentValues1 = new ContentValues();
        contentValues1.put("id", 1);
        contentValues1.put("name", "Tutoriel");
        contentValues1.put("created_at", dateNow);
        db.insert("Folder", OnConflictStrategy.IGNORE, contentValues1);

        ContentValues contentValues2 = new ContentValues();
        contentValues2.put("id", 2);
        contentValues2.put("name", "Défi");
        contentValues2.put("created_at", dateNow);
        db.insert("Folder", OnConflictStrategy.IGNORE, contentValues2);

        ContentValues contentValues3 = new ContentValues();
        contentValues3.put("id", 3);
        contentValues3.put("name", "Tournoi");
        contentValues3.put("created_at", dateNow);
        db.insert("Folder", OnConflictStrategy.IGNORE, contentValues3);

        ContentValues contentValues4 = new ContentValues();
        contentValues4.put("id", 4);
        contentValues4.put("name", "Paika");
        contentValues4.put("parent_id", 1); // Tutoriel
        contentValues4.put("created_at", dateNow);
        db.insert("Folder", OnConflictStrategy.IGNORE, contentValues4);

        ContentValues contentValues5 = new ContentValues();
        contentValues5.put("id", 5);
        contentValues5.put("name", "Vela");
        contentValues5.put("parent_id", 2); // Défi
        contentValues5.put("created_at", dateNow);
        db.insert("Folder", OnConflictStrategy.IGNORE, contentValues5);

    }

    private static void prepopulateDatabaseWithGames(SupportSQLiteDatabase db) {
        long dateNow = new Date().getTime();

        ContentValues contentValues1 = new ContentValues();
        contentValues1.put("id", 1);
        contentValues1.put("name", "test");
        contentValues1.put("created_at", dateNow);
        contentValues1.put("configs", "P:B\nC4:C5<\nD4:C4>C3<X\nB5:C5>C4>D4>D5<\nE2:D2>\nD5:D4<C5<C4<\nE7:D7>\nA3:B2>C2>\nC8:C7<D6<C6>C5>B5>B4>\nB9:B8<\nB4:A5<\nC2:B2<\nE9:D9<\nB2:C2<\nE6:E5<\nC2:B2<\nD7:C7<\nB8:A9<\nE8:D8<\nB2:C2<\nE5:D5<\nC2:B2<\nA5:B5<\nA1:A2<\nD5:C5<\nB1:A1<\nC5:B6<\nA1:B1<\nB6:B7<\nA2:A3<\nB7:B8<\nA9:B9<\nB8:B7<\nA8:A9<\nB7:B8<\nA9:A8<\nB8:C8<\nB1:A1<\nD8:E7<\nA1:A2<\nE7:D6<\nA2:A1<\nD6:E5<\nA1:A2<\nE5:E4<\nA3:A4<\nE4:E3<\nA4:A3<\nE3:D3<\nA2:A1<\nB5:A5<\nA3:A4>\nD3:C3<\nA1:A2<\nC3:D4<\nA4:A3<\nE1:D2<\nA2:A1<\nD2:D3<\nA1:B1<\nC8:C7<\nB1:A1<\nC7:B6<\nA1:B1<\nB6:B5<\nB1:C1<\nB5:B4<\nA3:A2<\nD3:D2<\nC1:C2<\nD2:E2<\nA2:A1<\nB4:B3<\nA1:A2<\nB3:B2<\nA2:A3<\nB2:C1<");
        contentValues1.put("image_index", 10);

        db.insert("Game", OnConflictStrategy.IGNORE, contentValues1);

    }

    private static void prepopulateDatabaseWithThemes(SupportSQLiteDatabase db) {
        long dateNow = new Date().getTime();

        ContentValues contentValues1 = new ContentValues();
        contentValues1.put("id", 1);
        contentValues1.put("name", "Perle");
        contentValues1.put("created_at", dateNow);
        contentValues1.put("folder_name", "pearl");

        db.insert("Theme", OnConflictStrategy.IGNORE, contentValues1);

        ContentValues contentValues2 = new ContentValues();
        contentValues2.put("id", 2);
        contentValues2.put("name", "Echec");
        contentValues2.put("created_at", dateNow);
        contentValues2.put("folder_name", "chessboard");

        db.insert("Theme", OnConflictStrategy.IGNORE, contentValues2);

        ContentValues contentValues3 = new ContentValues();
        contentValues3.put("id", 3);
        contentValues3.put("name", "Bouton");
        contentValues3.put("created_at", dateNow);
        contentValues3.put("folder_name", "button");

        db.insert("Theme", OnConflictStrategy.IGNORE, contentValues3);

    }
}
