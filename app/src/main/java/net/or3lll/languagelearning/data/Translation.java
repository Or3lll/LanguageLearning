package net.or3lll.languagelearning.data;

import com.orm.SugarRecord;

/**
 * Created by Or3lll on 29/11/2015.
 */
public class Translation extends SugarRecord {
    Word word1;
    Word word2;

    public Translation() {
    }

    public Translation(Word word1, Word word2) {
        this.word1 = word1;
        this.word2 = word2;
    }
}
