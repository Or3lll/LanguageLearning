package net.or3lll.languagelearning.data.dao;

/**
 * Created by X2014568 on 17/03/2016.
 */
public class WordDao {
    public static final String TABLE_NAME = "Word";
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
            "(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "text VARCHAR(100) NOT NULL, " +
            "subtext VARCHAR(100), " +
            "desc VARCHAR(100), " +
            "langId INTEGER, FOREIGN KEY(langId) REFERENCES " + LangDao.TABLE_NAME + "(id)" +
            ")";
}
