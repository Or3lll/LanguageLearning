package net.or3lll.languagelearning.configuration.shared;

import net.or3lll.languagelearning.data.Lang;

/**
 * Created by or3lll on 24/02/2016.
 */
public class UserLangAdapter extends LangAdapter {
    public UserLangAdapter() {
        mLangs = Lang.listAll(Lang.class);
    }
}
