package net.or3lll.languagelearning.data;

import com.orm.SugarRecord;

/**
 * Created by Or3lll on 29/11/2015.
 */
public class Word extends SugarRecord {
    public Lang lang;
    public String text;
    public String subText;
    public String desc;

    public Word() {
    }

    public Word(Lang lang, String text, String subText, String desc) {
        this.lang = lang;
        this.text = text;
        this.subText = subText;
        this.desc = desc;
    }
}
