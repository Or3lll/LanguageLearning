package net.or3lll.languagelearning.configuration.word.list;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import net.or3lll.languagelearning.R;
import net.or3lll.languagelearning.configuration.word.edit.EditWordActivity;
import net.or3lll.languagelearning.configuration.word.edit.EditWordFragment;
import net.or3lll.languagelearning.configuration.word.edit.TableTranslationListener;
import net.or3lll.languagelearning.shared.UserLangAdapter;
import net.or3lll.languagelearning.data.DataEventType;
import net.or3lll.languagelearning.data.Lang;
import net.or3lll.languagelearning.data.Translation;
import net.or3lll.languagelearning.data.Word;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WordListActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener,
        WordRecyclerViewAdapter.OnClickListener,
        TableWordListener,
        TableTranslationListener {

    private static final String TAG_EDIT_FRAGMENT = "edit_fragment";
    private static final String TAG_DELETE_DIALOG = "delete_dialog";

    @BindView(R.id.langSpinner) Spinner mLangSpinner;
    @BindView(R.id.emptyList) TextView emptyListText;
    private WordRecyclerViewAdapter mWordAdapter;

    @Nullable @BindView(R.id.edit_container) FrameLayout editContainer;
    private EditWordFragment mEditWordFragment;

    private TextToSpeech mTts;
    private boolean mIsTtsInit = false;
    private Word mWordTts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_word_list);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.word_activity_title);
        ab.setDisplayHomeAsUpEnabled(true);

        mLangSpinner.setAdapter(new UserLangAdapter());
        mLangSpinner.setOnItemSelectedListener(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mWordAdapter = new WordRecyclerViewAdapter((Lang) mLangSpinner.getSelectedItem(), this);
        recyclerView.setAdapter(mWordAdapter);
        updateList();
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mTts != null) {
            mTts.shutdown();
        }
    }

    private void updateList() {
        Lang lang = (Lang) mLangSpinner.getSelectedItem();
        mWordAdapter.updateLang(lang);

        if(mWordAdapter.getItemCount() == 0) {
            emptyListText.setVisibility(View.VISIBLE);
        }
        else {
            emptyListText.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.fab)
    public void onFabClicked() {
        if(editContainer != null) {
            mEditWordFragment = EditWordFragment.newInstance(null, (Lang) mLangSpinner.getSelectedItem());
            getSupportFragmentManager().beginTransaction().replace(R.id.edit_container, mEditWordFragment).commit();
        } else {
            Intent i = new Intent(this, EditWordActivity.class);
            i.putExtra(EditWordActivity.LANG_PARAM, (Lang) mLangSpinner.getSelectedItem());
            startActivity(i);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        updateList();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }

    @Override
    public void onClick(Word item) {
        mWordTts = item;
        if (mIsTtsInit) {
            speakWord();
        } else {
            mTts = new TextToSpeech(getApplicationContext(), status -> {
                if (status == TextToSpeech.SUCCESS) {
                    mIsTtsInit = true;
                    speakWord();
                }
            });
        }
    }

    private void speakWord() {
        String[] localeParts = mWordTts.lang.getIsoCode().split("_");
        Locale locale = new Locale.Builder().setLanguage(localeParts[0]).setRegion(localeParts[1]).build();
        mTts.setLanguage(locale);

        mTts.speak(mWordTts.text, TextToSpeech.QUEUE_FLUSH, null, "12");
    }

    @Override
    public void onEditClick(Word item) {
        if(editContainer != null) {
            mEditWordFragment = EditWordFragment.newInstance(item, null);
            getSupportFragmentManager().beginTransaction().replace(R.id.edit_container, mEditWordFragment).commit();
        } else {
            Intent i = new Intent(this, EditWordActivity.class);
            i.putExtra(EditWordActivity.WORD_PARAM, item);
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

        DialogFragment newFragment = DeleteWordDialogFragment.newInstance(item);
        newFragment.show(ft, TAG_DELETE_DIALOG);
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
    public void onTableTranslationEvent(DataEventType eventType, Translation translation) {
        if(mEditWordFragment != null) {
            mEditWordFragment.refreshTranslations();
        }
    }

    @Override
    public void onTableWordEvent(DataEventType eventType, Word word) {
        updateList();
        if(eventType == DataEventType.DELETE) {
            hideEdit();
        }
    }
}
