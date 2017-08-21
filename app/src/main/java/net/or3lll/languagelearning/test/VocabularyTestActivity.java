package net.or3lll.languagelearning.test;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import net.or3lll.languagelearning.R;
import net.or3lll.languagelearning.data.Lang;

import butterknife.ButterKnife;

public class VocabularyTestActivity extends AppCompatActivity {

    public static final String BUNDLE_LANG_SRC = "lang_src";
    public static final String BUNDLE_LANG_DST = "lang_dst";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

        // ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        Lang langSrc = getIntent().getParcelableExtra(BUNDLE_LANG_SRC);
        Lang langDst = getIntent().getParcelableExtra(BUNDLE_LANG_DST);

        actionBar.setTitle(langSrc.getName() + " -> " + langDst.getName());

        VocabularyTestFragment test =
                (VocabularyTestFragment) getSupportFragmentManager().findFragmentById(R.id.test);

        test.setLangs(langSrc, langDst);
    }
}
