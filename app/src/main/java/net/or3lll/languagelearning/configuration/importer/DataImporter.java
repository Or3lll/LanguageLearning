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
            JSONObject rootJson = new JSONObject(jsonText);
            analyzeLangs(rootJson);
            analyzeWords(rootJson);
            analyzeTranslations(rootJson);
        }
        catch (JSONException e) {

        }
    }

    private void analyzeLangs(JSONObject rootJson) {
        try {
            JSONArray langs = rootJson.getJSONArray("langs");
            for (int i = 0; i < langs.length(); i++) {
                try {
                    JSONObject jsonLang = langs.getJSONObject(i);
                    String isoCode = jsonLang.getString("isoCode");
                    if (Lang.getLangByIsoCode(isoCode) == null) {
                        String name = jsonLang.getString("name");
                        Lang lang = new Lang(name, isoCode);
                        if (lang.isValid()) {
                            langsToAdd.add(lang);
                        }
                    }
                } catch (JSONException e) { }
            }
        }
        catch (JSONException e) { }
    }

    private void analyzeWords(JSONObject rootJson) {
        try {
            JSONArray jsonWords = rootJson.getJSONArray("words");
            for (int i = 0; i < jsonWords.length(); i++) {
                try {
                    JSONObject jsonWord = jsonWords.getJSONObject(i);
                    String isoCode = jsonWord.getString("isoCode");

                    Lang lang = getLang(isoCode);
                    if (lang != null) {

                        String text = jsonWord.getString("text");
                        boolean isAlreadyExist = false;
                        for (Word word : Word.find(Word.class, "text=?", text)) {
                            if (word.lang.getId() == lang.getId()) {
                                isAlreadyExist = true;
                                break;
                            }
                        }

                        if (!isAlreadyExist) {
                            String subText = jsonWord.getString("subText");
                            String desc = jsonWord.getString("desc");
                            Word word = new Word(lang, text, subText, desc);
                            wordsToAdd.add(word);
                        }
                    }
                } catch (JSONException e) { }
            }
        } catch (JSONException e) { }
    }

    private void analyzeTranslations(JSONObject rootJson) {
        try {
            JSONArray translations = rootJson.getJSONArray("translations");
            for (int i = 0; i < translations.length(); i++) {
                try {
                    JSONObject jsonTranslation = translations.getJSONObject(i);

                    String isoCode1 = jsonTranslation.getString("isoCode1");
                    String text1 = jsonTranslation.getString("text1");
                    String isoCode2 = jsonTranslation.getString("isoCode2");
                    String text2 = jsonTranslation.getString("text2");

                    Lang lang1 = getLang(isoCode1);
                    Lang lang2 = getLang(isoCode2);

                    if (lang1 != null && lang2 != null) {
                        Word word1 = getWord(text1, lang1);
                        Word word2 = getWord(text2, lang2);

                        if (word1 != null && word2 != null) {
                            Translation translation = new Translation(word1, word2);
                            translationsToAdd.add(translation);
                        }
                    }
                } catch (JSONException e) { }
            }
        } catch (JSONException e) { }
    }


    private Lang getLang(String isoCode) {
        Lang lang = Lang.getLangByIsoCode(isoCode);
        if(lang == null) {
            for (Lang langToAdd : langsToAdd) {
                if(langToAdd.getIsoCode().equals(isoCode)) {
                    lang = langToAdd;
                    break;
                }
            }
        }

        return lang;
    }

    private Word getWord(String text, Lang lang) {
        Word word = Word.getWordByTextForLang(text, lang);
        if(word == null) {
            for (Word wordToAdd : wordsToAdd) {
                if(wordToAdd.text.equals(text) && wordToAdd.lang.getIsoCode().equals(lang.getIsoCode())) {
                    word = wordToAdd;
                    break;
                }
            }
        }

        return word;
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
