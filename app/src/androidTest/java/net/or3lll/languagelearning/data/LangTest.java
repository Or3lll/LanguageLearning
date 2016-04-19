package net.or3lll.languagelearning.data;

import android.app.Application;
import android.support.test.runner.AndroidJUnit4;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Or3lll on 19/04/2016.
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class LangTest extends ApplicationTestCase<Application> {
    public LangTest() {
        super(Application.class);
    }

    @Test
    public void isoCode_isValid() throws Exception {
        assertEquals(true, Lang.isValidIsoCode("aa_BB"));

        assertEquals(false, Lang.isValidIsoCode(""));
        assertEquals(false, Lang.isValidIsoCode("a_B"));
        assertEquals(false, Lang.isValidIsoCode("a_BB"));
        assertEquals(false, Lang.isValidIsoCode("a_BBB"));
        assertEquals(false, Lang.isValidIsoCode("aa_B"));
        assertEquals(false, Lang.isValidIsoCode("aaa_B"));
        assertEquals(false, Lang.isValidIsoCode("AA_BB"));
        assertEquals(false, Lang.isValidIsoCode("aa_bb"));
        assertEquals(false, Lang.isValidIsoCode("aaBB"));
        assertEquals(false, Lang.isValidIsoCode("aa-BB"));
    }
}