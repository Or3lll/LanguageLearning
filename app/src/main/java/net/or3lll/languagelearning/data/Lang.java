package net.or3lll.languagelearning.data;

import com.orm.SugarRecord;

/**
 * Created by Or3lll on 29/11/2015.
 */
public class Lang extends SugarRecord {
    public String name;
    public String isoCode;

    public Lang() {
    }

    public Lang(String name, String isoCode) {
        this.name = name;
        this.isoCode = isoCode;
    }
}
