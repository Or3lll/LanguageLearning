package net.or3lll.languagelearning.data.dao;

/**
 * Created by X2014568 on 17/03/2016.
 */
public class TranslationDao {
    public static final String TABLE_NAME = "Translation";
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
            "(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "word1Id INTEGER, " +
            "word2Id INTEGER, " +
            "FOREIGN KEY(word1Id) REFERENCES " + WordDao.TABLE_NAME + "(id), " +
            "FOREIGN KEY(word2Id) REFERENCES " + WordDao.TABLE_NAME + "(id)" +
            ")";
}
