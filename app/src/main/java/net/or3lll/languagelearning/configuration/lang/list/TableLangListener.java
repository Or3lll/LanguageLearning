package net.or3lll.languagelearning.configuration.lang.list;

import net.or3lll.languagelearning.data.DataEventType;
import net.or3lll.languagelearning.data.Lang;

/**
 * Created by or3lll on 23/03/2016.
 */
public interface TableLangListener {
    void onTableLangEvent(DataEventType eventType, Lang lang);
}
