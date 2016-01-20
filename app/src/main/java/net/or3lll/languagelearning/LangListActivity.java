package net.or3lll.languagelearning;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import net.or3lll.languagelearning.configuration.lang.AddLangActivity;
import net.or3lll.languagelearning.data.Lang;

public class LangListActivity extends AppCompatActivity implements LangListFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lang_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.title_activity_languages);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClickAddLang() {
        Intent i = new Intent(this, AddLangActivity.class);
        startActivity(i);
    }

    @Override
    public void onListFragmentInteraction(Lang item) {

    }
}
