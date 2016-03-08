package net.or3lll.languagelearning.configuration.word;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import net.or3lll.languagelearning.R;
import net.or3lll.languagelearning.data.Translation;

/**
 * Created by X2014568 on 03/03/2016.
 */
public class EditWordActivity
        extends AppCompatActivity
        implements EditWordFragment.OnFragmentInteractionListener,
        AddTranslationDialogFragment.OnAddTranslationListener,
        DeleteTranslationDialogFragment.OnDeleteTranslationListener {

    public static String WORD_ID_PARAM = "WORD_ID";
    public static String LANG_ID_PARAM = "LANG_ID";

    private EditWordFragment mEditWordFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_word);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.title_activity_word);
        ab.setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState == null) {
            long wordId = getIntent().getLongExtra(WORD_ID_PARAM, -1);
            long langId = getIntent().getLongExtra(LANG_ID_PARAM, -1);

            mEditWordFragment = EditWordFragment.newInstance(wordId, langId);
            getSupportFragmentManager().beginTransaction().add(R.id.content, mEditWordFragment).commit();
        }
    }

    @Override
    public void onWordAdded() {
    }

    @Override
    public void onWordUpdated() {
    }

    @Override
    public void onTranslationAdd() {
        if(mEditWordFragment != null) {
            mEditWordFragment.refreshTranslations();
        }
    }

    @Override
    public void onTranslationDeleted(Translation translation) {
        if(mEditWordFragment != null) {
            mEditWordFragment.refreshTranslations();
        }
    }
}
