package net.or3lll.languagelearning.data;

import android.app.Application;
import android.support.test.runner.AndroidJUnit4;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.orm.SugarRecord;

import net.or3lll.languagelearning.configuration.importer.DataImporter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by or3lll on 05/09/2016.
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class ImporterTest extends ApplicationTestCase<Application> {
    public ImporterTest() {
        super(Application.class);
    }

    @Test
    public void importLangsOnly() throws Exception {
        clearData();

        String dataToImport =
            "{" +
            "\"langs\": [{" +
            "\"isoCode\": \"es_ES\"," +
            "\"name\": \"Espagnol\"" +
            "}, {" +
            "\"isoCode\": \"pt_BR\"," +
            "\"name\": \"Portugais du Brésil\"" +
            "}]" +
            "}";

        DataImporter importer = new DataImporter();
        importer.load(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(dataToImport.getBytes()))));
        importer.apply();

        assertEquals(1, SugarRecord.find(Lang.class, "iso_code=?", "es_ES").size());
        assertEquals(1, SugarRecord.find(Lang.class, "iso_code=?", "pt_BR").size());
    }

    @Test
    public void importWordsOnly() throws Exception {
        clearData();

        SugarRecord.save(new Lang("Français", "fr_FR"));

        String dataToImport =
            "{" +
            "\"words\": [{" +
            "\"isoCode\": \"fr_FR\"," +
            "\"text\": \"voiture\"," +
            "\"subText\": \"\"," +
            "\"desc\": \"\"" +
            "}, {" +
            "\"isoCode\": \"fr_FR\"," +
            "\"text\": \"souris\"," +
            "\"subText\": \"\"," +
            "\"desc\": \"Pour ordinateur\"" +
            "}]" +
            "}";

        DataImporter importer = new DataImporter();
        importer.load(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(dataToImport.getBytes()))));
        importer.apply();

        assertEquals(1, Word.find(Word.class, "text=?", "voiture").size());
        assertEquals(1, Word.find(Word.class, "text=?", "souris").size());
    }

    @Test
    public void importTranslationsOnly() throws Exception {
        clearData();

        Lang frenchLang = new Lang("Français", "fr_FR");
        Lang russianLang = new Lang("Russe", "ru_RU");
        SugarRecord.save(frenchLang);
        SugarRecord.save(russianLang);
        Word.save(new Word(frenchLang, "voiture", "", ""));
        Word.save(new Word(russianLang, "машина", "", ""));

        String dataToImport =
            "{" +
            "\"translations\": [{" +
            "\"isoCode1\": \"fr_FR\"," +
            "\"text1\": \"voiture\"," +
            "\"isoCode2\": \"ru_RU\"," +
            "\"text2\": \"машина\"" +
            "}]" +
            "}";

        DataImporter importer = new DataImporter();
        importer.load(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(dataToImport.getBytes()))));
        importer.apply();

        List<Translation> translations = Translation.listAll(Translation.class);
        assertEquals(1, translations.size());

        Translation translation = translations.get(0);
        assertTrue((translation.word1.text.equals("voiture") && translation.word1.lang.getIsoCode().equals("fr_FR")
                && translation.word2.text.equals("машина") && translation.word2.lang.getIsoCode().equals("ru_RU"))
                ||
                (translation.word1.text.equals("машина") && translation.word1.lang.getIsoCode().equals("ru_RU")
                && translation.word2.text.equals("voiture") && translation.word2.lang.getIsoCode().equals("fr_FR"))
        );
    }

    @Test
    public void importAllTypes() throws Exception {
        clearData();

        String initJson = "{" +
            "\"langs\": [{" +
            "\"isoCode\": \"fr_FR\"," +
            "\"name\": \"Français\"" +
            "}, {" +
            "\"isoCode\": \"ja_JP\"," +
            "\"name\": \"Japonais\"" +
            "}]," +
            "\"words\": [{" +
            "\"isoCode\": \"fr_FR\"," +
            "\"text\": \"je\"," +
            "\"subText\": \"\"," +
            "\"desc\": \"\"" +
            "}, {" +
            "\"isoCode\": \"ja_JP\"," +
            "\"text\": \"わたし\"," +
            "\"subText\": \"\"," +
            "\"desc\": \"\"" +
            "}, {" +
            "\"isoCode\": \"fr_FR\"," +
            "\"text\": \"moi\"," +
            "\"subText\": \"\"," +
            "\"desc\": \"\"" +
            "}, {" +
            "\"isoCode\": \"fr_FR\"," +
            "\"text\": \"nous\"," +
            "\"subText\": \"\"," +
            "\"desc\": \"\"" +
            "}, {" +
            "\"isoCode\": \"ja_JP\"," +
            "\"text\": \"わたしたち\"," +
            "\"subText\": \"\"," +
            "\"desc\": \"\"" +
            "}]," +
            "\"translations\": [{" +
            "\"isoCode1\": \"fr_FR\"," +
            "\"text1\": \"je\"," +
            "\"isoCode2\": \"ja_JP\"," +
            "\"text2\": \"わたし\"" +
            "}]" +
            "}";

        DataImporter importer = new DataImporter();
        importer.load(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(initJson.getBytes()))));
        importer.apply();

        List<Translation> translations = Translation.listAll(Translation.class);
        assertEquals(1, translations.size());

        Translation translation = translations.get(0);
        assertTrue((translation.word1.text.equals("わたし") && translation.word1.lang.getIsoCode().equals("ja_JP")
                && translation.word2.text.equals("je") && translation.word2.lang.getIsoCode().equals("fr_FR"))
                ||
                (translation.word1.text.equals("je") && translation.word1.lang.getIsoCode().equals("fr_FR")
                        && translation.word2.text.equals("わたし") && translation.word2.lang.getIsoCode().equals("ja_JP"))
        );
    }

    private void clearData() {
        Translation.deleteAll(Translation.class);
        Word.deleteAll(Word.class);
        SugarRecord.deleteAll(Lang.class);
    }
}
