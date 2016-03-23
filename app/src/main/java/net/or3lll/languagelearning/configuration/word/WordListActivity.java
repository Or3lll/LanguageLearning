package net.or3lll.languagelearning.configuration.word;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import net.or3lll.languagelearning.configuration.shared.UserLangAdapter;
import net.or3lll.languagelearning.data.Lang;
import net.or3lll.languagelearning.data.Translation;
import net.or3lll.languagelearning.data.Word;

import java.util.ArrayList;
import java.util.List;

public class WordListActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener,
        WordRecyclerViewAdapter.OnClickListener,
        EditWordFragment.OnFragmentInteractionListener,
        DeleteWordDialogFragment.OnDeleteWordListener,
        AddTranslationDialogFragment.OnAddTranslationListener,
        DeleteTranslationDialogFragment.OnDeleteTranslationListener {

    private static final String TAG_EDIT_FRAGMENT = "edit_fragment";
    private static final String TAG_DELETE_DIALOG = "delete_dialog";

    private Spinner mLangSpinner;
    private TextView emptyListText;
    private WordRecyclerViewAdapter mWordAdapter;
    private FrameLayout editContainer;
    private EditWordFragment mEditWordFragment;

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
        updateList();

        editContainer = (FrameLayout) findViewById(R.id.edit_container);
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateList();
    }

    private void updateList() {
        // TODO Mieux gérer la récupération quand j'utiliserai les cursors
        Lang lang = (Lang) mLangSpinner.getSelectedItem();
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
                mEditWordFragment = EditWordFragment.newInstance(-1L, (Lang) mLangSpinner.getSelectedItem());
                getSupportFragmentManager().beginTransaction().replace(R.id.edit_container, mEditWordFragment).commit();
            } else {
                Intent i = new Intent(this, EditWordActivity.class);
                i.putExtra(EditWordActivity.LANG_PARAM, (Lang) mLangSpinner.getSelectedItem());
                startActivity(i);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        updateList();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }

    @Override
    public void onClick(Word item) {
        if(editContainer != null) {
             mEditWordFragment = EditWordFragment.newInstance(item.getId(), null);
            getSupportFragmentManager().beginTransaction().replace(R.id.edit_container, mEditWordFragment).commit();
        } else {
            Intent i = new Intent(this, EditWordActivity.class);
            i.putExtra(EditWordActivity.WORD_ID_PARAM, item.getId());
            startActivity(i);
        }
    }

    @Override
    public void onLongClick(Word item) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(TAG_DELETE_DIALOG);
        if (prev != null) {
            ft.remove(prev);
        }

        DialogFragment newFragment = DeleteWordDialogFragment.newInstance(item.getId());
        newFragment.show(ft, TAG_DELETE_DIALOG);
    }

    @Override
    public void onWordAdded() {
        updateList();
    }

    @Override
    public void onWordUpdated() {
        updateList();
    }

    private void hideEdit() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(TAG_EDIT_FRAGMENT);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.commit();
    }

    @Override
    public void onWordDeleted(Word word) {
        updateList();
        hideEdit();
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
