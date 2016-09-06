package net.or3lll.languagelearning.configuration.exporter;

import com.orm.SugarRecord;

import net.or3lll.languagelearning.data.Lang;
import net.or3lll.languagelearning.data.Translation;
import net.or3lll.languagelearning.data.Word;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by X2014568 on 06/09/2016.
 */
public class DataExporter {
    public String export() {
        JSONObject root = new JSONObject();

        try {
            JSONArray jsonLangs = new JSONArray();

            for (Lang lang : SugarRecord.listAll(Lang.class)) {
                JSONObject jsonLang = new JSONObject();
                jsonLang.put("isoCode", lang.getIsoCode());
                jsonLang.put("name", lang.getName());
                jsonLangs.put(jsonLang);
            }

            root.put("langs", jsonLangs);
        }
        catch (JSONException e) { }

        try {
            JSONArray jsonWords = new JSONArray();

            for (Word word : SugarRecord.listAll(Word.class)) {
                JSONObject jsonWord = new JSONObject();
                jsonWord.put("isoCode", word.lang.getIsoCode());
                jsonWord.put("text", word.text);
                jsonWord.put("subText", word.subText);
                jsonWord.put("desc", word.desc);
                jsonWords.put(jsonWord);
            }

            root.put("words", jsonWords);
        }
        catch (JSONException e) { }

        try {
            JSONArray jsonTranslations = new JSONArray();

            for (Translation translation : SugarRecord.listAll(Translation.class)) {
                JSONObject jsonTranslation = new JSONObject();
                jsonTranslation.put("isoCode1", translation.word1.lang.getIsoCode());
                jsonTranslation.put("text1", translation.word1.text);
                jsonTranslation.put("isoCode2", translation.word2.lang.getIsoCode());
                jsonTranslation.put("text2", translation.word2.text);
                jsonTranslations.put(jsonTranslation);
            }

            root.put("translations", jsonTranslations);
        }
        catch (JSONException e) { }

        return root.toString();
    }
}
