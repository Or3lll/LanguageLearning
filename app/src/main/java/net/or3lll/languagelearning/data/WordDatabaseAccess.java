package net.or3lll.languagelearning.data;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.or3lll.languagelearning.data.dao.LangDao;
import net.or3lll.languagelearning.data.dao.TranslationDao;
import net.or3lll.languagelearning.data.dao.WordDao;

/**
 * Created by X2014568 on 16/03/2016.
 */
public class WordDatabaseAccess extends SQLiteOpenHelper {
    private static final String DB_NAME = "words.db";
    private static final int DB_VERSION = 2;


    public WordDatabaseAccess(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        create2(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion <= 1 && newVersion >=2) {
            upgrade1To2(db);
        }
    }

    private void create2(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            db.execSQL(LangDao.CREATE_TABLE);
            db.execSQL(WordDao.CREATE_TABLE);
            db.execSQL(TranslationDao.CREATE_TABLE);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private void upgrade1To2(SQLiteDatabase db) {
        // Destroy SugarORM table
        db.execSQL("DROP TABLE IF EXISTS Lang");
        db.execSQL("DROP TABLE IF EXISTS Word");
        db.execSQL("DROP TABLE IF EXISTS Translation");

        // Create new full SQL table
        create2(db);
    }
}
