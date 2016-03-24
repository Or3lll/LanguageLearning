package net.or3lll.languagelearning.configuration.lang;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import net.or3lll.languagelearning.R;
import net.or3lll.languagelearning.data.DataEventType;
import net.or3lll.languagelearning.data.Lang;

public class EditLangActivity extends AppCompatActivity implements TableLangListener {

    public static String LANG_PARAM = "LANG_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_lang);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.title_activity_lang);
        ab.setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState == null) {
            Lang lang = getIntent().getParcelableExtra(LANG_PARAM);
            EditLangFragment editLangFragment = EditLangFragment.newInstance(lang);
            getSupportFragmentManager().beginTransaction().add(R.id.content, editLangFragment).commit();
        }
    }

    @Override
    public void onTableLangEvent(DataEventType eventType, Lang lang) {
        finish();
    }
}
