package net.or3lll.languagelearning.data;

import android.app.Application;
import android.support.test.runner.AndroidJUnit4;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.orm.SugarApp;
import com.orm.SugarRecord;

import net.or3lll.languagelearning.configuration.importer.DataImporter;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

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
}
