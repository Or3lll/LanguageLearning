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
        assertEquals(true, new Lang("aaa", "aa_BB", "").isValid());

        assertEquals(false, new Lang("aaa", "", "").isValid());
        assertEquals(false, new Lang("aaa", "a_B", "").isValid());
        assertEquals(false, new Lang("aaa", "a_BB", "").isValid());
        assertEquals(false, new Lang("aaa", "a_BBB", "").isValid());
        assertEquals(false, new Lang("aaa", "aa_B", "").isValid());
        assertEquals(false, new Lang("aaa", "aaa_B", "").isValid());
        assertEquals(false, new Lang("aaa", "AA_BB", "").isValid());
        assertEquals(false, new Lang("aaa", "aa_bb", "").isValid());
        assertEquals(false, new Lang("aaa", "aaBB", "").isValid());
        assertEquals(false, new Lang("aaa", "aa-BB", "").isValid());
    }
}