package net.or3lll.languagelearning.configuration.importer;

import com.orm.SugarRecord;

import net.or3lll.languagelearning.data.Lang;

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
    private List<Lang> langsToAdd = new ArrayList<Lang>();


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
                        Lang lang = new Lang();
                        lang.setIsoCode(isoCode);
                        lang.setName(name);
                        if(lang.isValid()) {
                            langsToAdd.add(lang);
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
    }
}
