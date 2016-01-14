package net.or3lll.languagelearning.configuration.lang;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.or3lll.languagelearning.R;

public class AddLangActivity extends AppCompatActivity implements AddLangFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lang);
    }

    @Override
    public void onLanguageAdded() {
        finish();
    }
}
