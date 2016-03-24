package net.or3lll.languagelearning.configuration.shared;

import android.util.SparseArray;

import net.or3lll.languagelearning.data.Lang;

/**
 * Created by or3lll on 24/02/2016.
 */
public class UserLangAdapter extends LangAdapter {
    public UserLangAdapter() {
        mValuesNumber = (int) Lang.count(Lang.class);
        mValues = new SparseArray<>(mValuesNumber);
    }
}
