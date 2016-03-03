package net.or3lll.languagelearning.configuration.word;

import android.content.Intent;
import android.provider.UserDictionary;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import net.or3lll.languagelearning.R;
import net.or3lll.languagelearning.configuration.lang.EditLangFragment;
import net.or3lll.languagelearning.configuration.shared.UserLangAdapter;
import net.or3lll.languagelearning.data.Lang;
import net.or3lll.languagelearning.data.Word;

import java.util.ArrayList;
import java.util.List;

public class WordListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, WordRecyclerViewAdapter.OnClickListener {

    private Spinner mLangSpinner;
    private TextView emptyListText;
    private WordRecyclerViewAdapter mWordAdapter;
    private FrameLayout editContainer;

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
        mLangSpinner.setAdapter(new UserLangAdapter());
        mLangSpinner.setOnItemSelectedListener(this);

        emptyListText = (TextView) findViewById(R.id.emptyList);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mWordAdapter = new WordRecyclerViewAdapter(Word.listAll(Word.class), this);
        recyclerView.setAdapter(mWordAdapter);
        updateList((Lang) mLangSpinner.getSelectedItem());

        editContainer = (FrameLayout) findViewById(R.id.edit_container);
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateList(Lang.findById(Lang.class, mLangSpinner.getSelectedItemId()));
    }

    private void updateList(Lang lang) {
        // TODO Mieux gérer la récupération quand j'utiliserai les cursors
        List<Word> words = new ArrayList<>();
        for (Word word : Word.listAll(Word.class)) {
            if(word.lang.getId() == lang.getId()) {
                words.add(word);
            }
        }

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
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_add_word) {
            if(editContainer != null) {
                EditWordFragment editWordFragment = EditWordFragment.newInstance(-1L, mLangSpinner.getSelectedItemId());
                getSupportFragmentManager().beginTransaction().add(R.id.content, editWordFragment).commit();
            } else {
                Intent i = new Intent(this, EditWordActivity.class);
                i.putExtra(EditWordActivity.LANG_ID_PARAM, mLangSpinner.getSelectedItemId());
                startActivity(i);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        updateList((Lang) parent.getSelectedItem());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }

    @Override
    public void onClick(Word item) {
        if(editContainer != null) {
            EditWordFragment editWordFragment = EditWordFragment.newInstance(item.getId(), -1L);
            getSupportFragmentManager().beginTransaction().add(R.id.content, editWordFragment).commit();
        } else {
            Intent i = new Intent(this, EditWordActivity.class);
            i.putExtra(EditWordActivity.WORD_ID_PARAM, item.getId());
            startActivity(i);
        }
    }

    @Override
    public void onLongClick(Word item) {
        // Display delete dialog fragment
    }
}
