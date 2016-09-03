package net.or3lll.languagelearning.configuration.shared;

import android.util.SparseArray;

import com.orm.SugarRecord;

import net.or3lll.languagelearning.data.Lang;

import java.util.ArrayList;

/**
 * Created by or3lll on 24/02/2016.
 */
public class AvailableDefaultLangAdapter extends LangAdapter {

    public AvailableDefaultLangAdapter() {
        mValues = new SparseArray<>();

        int index = 0;
        for (Lang lang : Lang.defaultLangs) {
            if (SugarRecord.count(Lang.class, "iso_Code = ?", new String [] { lang.getIsoCode() }) == 0) {
                mValues.append(index++, lang);
            }
        }

        mValuesNumber = index;
    }

    @Override
    public long getItemId(int position) {
        return -1;
    }
}
