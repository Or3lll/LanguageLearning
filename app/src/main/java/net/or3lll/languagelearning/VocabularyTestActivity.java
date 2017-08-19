package net.or3lll.languagelearning;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VocabularyTestActivity extends AppCompatActivity {

    public static final String BUNDLE_LANG_SRC = "lang_src";
    public static final String BUNDLE_LANG_DST = "lang_dst";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

        VocabularyTestFragment test =
                (VocabularyTestFragment) getSupportFragmentManager().findFragmentById(R.id.test);

        test.setLangs(getIntent().getParcelableExtra(BUNDLE_LANG_SRC),
                getIntent().getParcelableExtra(BUNDLE_LANG_DST));
    }
}
