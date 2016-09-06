package net.or3lll.languagelearning.data;

import android.app.Application;
import android.support.test.runner.AndroidJUnit4;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.orm.SugarRecord;

import net.or3lll.languagelearning.configuration.exporter.DataExporter;
import net.or3lll.languagelearning.configuration.importer.DataImporter;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by X2014568 on 06/09/2016.
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class ExportTest extends ApplicationTestCase<Application> {
    public ExportTest() {
        super(Application.class);
    }

    @Test
    public void export() throws Exception {
        clearData();

        Lang frenchLang = new Lang("Français", "fr_FR");
        Lang russianLang = new Lang("Russe", "ru_RU");
        SugarRecord.save(frenchLang);
        SugarRecord.save(russianLang);
        Word word1 = new Word(frenchLang, "voiture", "", "");
        Word word2 = new Word(russianLang, "машина", "", "");
        Word.save(word1);
        Word.save(word2);
        Translation.save(new Translation(word1, word2));

        String exportedJson = (new DataExporter()).export();

        clearData();

        DataImporter importer = new DataImporter();
        importer.load(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(exportedJson.getBytes()))));
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

    private void clearData() {
        Translation.deleteAll(Translation.class);
        Word.deleteAll(Word.class);
        SugarRecord.deleteAll(Lang.class);
    }
}
