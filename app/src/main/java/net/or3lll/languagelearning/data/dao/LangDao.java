package net.or3lll.languagelearning.data.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.or3lll.languagelearning.data.WordDatabaseAccess;

/**
 * Created by X2014568 on 17/03/2016.
 */
public class LangDao {
    public static final String TABLE_NAME = "Lang";
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
            "(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name VARCHAR(50) NOT NULL, " +
            "isoCode CHAR(5) NOT NULL" +
            ")";

    public static long count(Context context) {
        long count = 0;

        SQLiteDatabase readableDb = (new WordDatabaseAccess(context)).getReadableDatabase();
        Cursor c = readableDb.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);
        if(c.moveToFirst()) {
            count = c.getLong(0);
        }
        c.close();

        return count;
    }
}
