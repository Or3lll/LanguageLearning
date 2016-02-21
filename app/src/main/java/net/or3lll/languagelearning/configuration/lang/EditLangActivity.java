package net.or3lll.languagelearning.configuration.lang;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.or3lll.languagelearning.R;

public class EditLangActivity extends AppCompatActivity implements EditLangFragment.OnFragmentInteractionListener {

    public static String LANG_ID_PARAM = "LANG_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_lang);

        if(savedInstanceState == null) {
            long langId = getIntent().getLongExtra(LANG_ID_PARAM, -1);
            EditLangFragment editLangFragment = EditLangFragment.newInstance(langId, this);
            getSupportFragmentManager().beginTransaction().add(R.id.content, editLangFragment).commit();
        }
    }

    @Override
    public void onLanguageAdded() {
        finish();
    }
}
