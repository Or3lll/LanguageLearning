package net.or3lll.languagelearning.configuration.importer;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import net.or3lll.languagelearning.data.Lang;
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
    }
}
