package net.or3lll.languagelearning.configuration.word;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import net.or3lll.languagelearning.R;
import net.or3lll.languagelearning.configuration.lang.LangRecyclerViewAdapter;
import net.or3lll.languagelearning.data.Lang;
import net.or3lll.languagelearning.data.Word;

import java.util.List;

public class WordListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner mLangSpinner;
    private TextView emptyListText;
    private WordRecyclerViewAdapter mWordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.title_activity_word);
        ab.setDisplayHomeAsUpEnabled(true);

        mLangSpinner = (Spinner) findViewById(R.id.langSpinner);
        mLangSpinner.setAdapter(new LangAdapter());
        mLangSpinner.setOnItemSelectedListener(this);

        emptyListText = (TextView) findViewById(R.id.emptyList);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mWordAdapter = new WordRecyclerViewAdapter(Word.listAll(Word.class), null);
        recyclerView.setAdapter(mWordAdapter);
        updateList((Lang) mLangSpinner.getSelectedItem());
    }

    private void updateList(Lang lang) {

        List<Word> words = Word.listAll(Word.class);

        words.add(new Word(lang, "Saucisse" + lang.isoCode, "", ""));
        words.add(new Word(lang, "Voiture", "", ""));
        words.add(new Word(lang, "Chien", "", ""));
        words.add(new Word(lang, "Quille", "", ""));
        words.add(new Word(lang, "Guitare", "", ""));
        words.add(new Word(lang, "Armoire", "", ""));
        words.add(new Word(lang, "Sac", "", ""));
        words.add(new Word(lang, "Lampe", "", ""));

        if(words.size() == 0) {
            emptyListText.setVisibility(View.VISIBLE);
        }
        else {
            emptyListText.setVisibility(View.GONE);
        }

        mWordAdapter.setWords(words);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_word, menu);
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        updateList((Lang) parent.getSelectedItem());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
