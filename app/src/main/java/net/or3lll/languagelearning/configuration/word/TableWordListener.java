package net.or3lll.languagelearning.configuration.word;

import net.or3lll.languagelearning.data.DataEventType;
import net.or3lll.languagelearning.data.Word;

/**
 * Created by or3lll on 23/03/2016.
 */
public interface TableWordListener {
    void onTableWordEvent(DataEventType eventType, Word word);
}
