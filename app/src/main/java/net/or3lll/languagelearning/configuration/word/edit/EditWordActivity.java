package net.or3lll.languagelearning.configuration.word.edit;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import net.or3lll.languagelearning.R;
import net.or3lll.languagelearning.data.DataEventType;
import net.or3lll.languagelearning.data.Lang;
import net.or3lll.languagelearning.data.Translation;
import net.or3lll.languagelearning.data.Word;

/**
 * Created by X2014568 on 03/03/2016.
 */
public class EditWordActivity
        extends AppCompatActivity
        implements TableTranslationListener {

    public static String WORD_PARAM = "WORD";
    public static String LANG_PARAM = "LANG";

    private EditWordFragment mEditWordFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.word_activity_title);
        ab.setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState == null) {
            Word word = getIntent().getParcelableExtra(WORD_PARAM);
            Lang lang = getIntent().getParcelableExtra(LANG_PARAM);

            mEditWordFragment = EditWordFragment.newInstance(word, lang);
            getSupportFragmentManager().beginTransaction().add(R.id.content, mEditWordFragment).commit();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onTableTranslationEvent(DataEventType eventType, Translation translation) {
        if(mEditWordFragment != null) {
            mEditWordFragment.refreshTranslations();
        }
    }
}
