package net.or3lll.languagelearning.configuration.word;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import net.or3lll.languagelearning.R;

/**
 * Created by X2014568 on 03/03/2016.
 */
public class EditWordActivity extends AppCompatActivity implements EditWordFragment.OnFragmentInteractionListener {

    public static String WORD_ID_PARAM = "WORD_ID";
    public static String LANG_ID_PARAM = "LANG_ID";

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

            EditWordFragment editWordFragment = EditWordFragment.newInstance(wordId, langId);
            getSupportFragmentManager().beginTransaction().add(R.id.content, editWordFragment).commit();
        }
    }

    @Override
    public void onWordAdded() {
    }

    @Override
    public void onWordUpdated() {
    }
}
