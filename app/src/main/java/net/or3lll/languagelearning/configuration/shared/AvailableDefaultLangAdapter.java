package net.or3lll.languagelearning.configuration.shared;

import net.or3lll.languagelearning.data.Lang;

import java.util.ArrayList;

/**
 * Created by or3lll on 24/02/2016.
 */
public class AvailableDefaultLangAdapter extends LangAdapter {

    public AvailableDefaultLangAdapter() {
        mLangs = new ArrayList<Lang>();

        for (Lang lang : Lang.defaultLangs) {
            if (Lang.count(Lang.class, "iso_Code = ?", new String [] { lang.isoCode }) == 0) {
                mLangs.add(lang);
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return -1;
    }
}
