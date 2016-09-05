package net.or3lll.languagelearning.configuration.importer;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import net.or3lll.languagelearning.data.Lang;
import net.or3lll.languagelearning.data.Translation;
import net.or3lll.languagelearning.data.Word;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by or3lll on 05/09/2016.
 */
public class DataImporter {
    private List<Lang> langsToAdd = new ArrayList<>();
    private List<Word> wordsToAdd = new ArrayList<>();
    private List<Translation> translationsToAdd = new ArrayList<>();

    public void load(BufferedReader reader) {
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            String json = sb.toString();
            analyzeJson(json);

        }
        catch (IOException e) {

        }
    }

    private void analyzeJson(String jsonText) {
        try {
            JSONObject json = new JSONObject(jsonText);
            try {
                JSONArray langs = json.getJSONArray("langs");
                for (int i = 0; i < langs.length(); i++) {
                    JSONObject jsonLang = langs.getJSONObject(i);
                    String isoCode = jsonLang.getString("isoCode");
                    if(SugarRecord.find(Lang.class, "iso_code=?", isoCode).size() == 0) {
                        String name = jsonLang.getString("name");
                        Lang lang = new Lang(name, isoCode);
                        if(lang.isValid()) {
                            langsToAdd.add(lang);
                        }
                    }
                }
            }
            catch (JSONException e) {

            }

            try {
                JSONArray jsonWords = json.getJSONArray("words");
                for (int i = 0; i < jsonWords.length(); i++) {
                    JSONObject jsonWord = jsonWords.getJSONObject(i);
                    String isoCode = jsonWord.getString("isoCode");

                    List<Lang> langs = SugarRecord.find(Lang.class, "iso_code=?", isoCode);
                    if(langs.size() == 1) {
                        Lang lang = langs.get(0);

                        String text = jsonWord.getString("text");
                        List<Word> words = Word.find(Word.class, "text=?", text);
                        boolean isAlreadyExist = false;
                        for (Word word : words) {
                            if(word.lang.getId() == lang.getId()) {
                                isAlreadyExist = true;
                                break;
                            }
                        }

                        if(!isAlreadyExist) {
                            String subText = jsonWord.getString("subText");
                            String desc = jsonWord.getString("desc");
                            Word word = new Word(lang, text, subText, desc);
                            wordsToAdd.add(word);
                        }
                    }
                }
            }
            catch (JSONException e) {

            }

            try {
                JSONArray translations = json.getJSONArray("translations");
                for (int i = 0; i < translations.length(); i++) {
                    JSONObject jsonTranslation = translations.getJSONObject(i);

                    String isoCode1 = jsonTranslation.getString("isoCode1");
                    String text1 = jsonTranslation.getString("text1");
                    String isoCode2 = jsonTranslation.getString("isoCode2");
                    String text2 = jsonTranslation.getString("text2");

                    List<Lang> langs = SugarRecord.find(Lang.class, "iso_code=?", isoCode1);
                    if(langs.size() == 1) {
                        Lang lang1 = langs.get(0);
                        List<Word> words = Word.find(Word.class, "lang=? AND text=?", new String[]{ lang1.getId().toString(), text1 });
                        if(words.size() == 1) {
                            Word word1 = words.get(0);
                            langs = SugarRecord.find(Lang.class, "iso_code=?", isoCode2);
                            if (langs.size() == 1) {
                                Lang lang2 = langs.get(0);
                                words = Word.find(Word.class, "lang=? AND text=?", new String[]{ lang2.getId().toString(), text2 });
                                if(words.size() == 1) {
                                    Word word2 = words.get(0);

                                    Translation translation = new Translation(word1, word2);
                                    translationsToAdd.add(translation);
                                }
                            }
                        }
                    }
                }
            }
            catch (JSONException e) {

            }
        }
        catch (JSONException e) {

        }
    }

    public void apply() {
        for (Lang lang : langsToAdd) {
            SugarRecord.save(lang);
        }

        for (Word word : wordsToAdd) {
            word.save();
        }

        for (Translation translation : translationsToAdd) {
            translation.save();
        }
    }
}
