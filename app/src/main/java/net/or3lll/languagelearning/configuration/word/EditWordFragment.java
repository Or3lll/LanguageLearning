package net.or3lll.languagelearning.configuration.word;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import net.or3lll.languagelearning.R;
import net.or3lll.languagelearning.configuration.shared.UserLangAdapter;
import net.or3lll.languagelearning.data.DataEventType;
import net.or3lll.languagelearning.data.Lang;
import net.or3lll.languagelearning.data.Translation;
import net.or3lll.languagelearning.data.Word;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by X2014568 on 03/03/2016.
 */
public class EditWordFragment extends Fragment implements AdapterView.OnItemSelectedListener,
        TranslationRecyclerViewAdapter.OnClickListener {
    public static String WORD_PARAM = "WORD_ID";
    public static String LANG_PARAM = "LANG";

    private static final String TAG_ADD_TRANSLATION_DIALOG = "add_translation_dialog";
    private static final String TAG_DELETE_TRANSLATION_DIALOG = "delete_translation_dialog";

    private Unbinder unbinder;

    @BindView(R.id.langSpinner) Spinner mLangSpinner;
    @BindView(R.id.name_edit) EditText mNameEdit;
    @BindView(R.id.sub_name_edit) EditText mSubNameEdit;
    @BindView(R.id.desc_edit) EditText mDescEdit;
    @BindView(R.id.add_btn) Button mAddButton;
    @BindView(R.id.translations_layout) ViewGroup mTranslationsLayout;
    @BindView(R.id.translation_list) RecyclerView mTranslationRecycler;
    private TranslationRecyclerViewAdapter mTranslationAdapter;

    private TableWordListener mListener;

    private Word mWord;
    private Lang mLang;


    public static EditWordFragment newInstance(Word word, Lang lang) {
        EditWordFragment fragment = new EditWordFragment();
        Bundle args = new Bundle();
        args.putParcelable(WORD_PARAM, word);
        args.putParcelable(LANG_PARAM, lang);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_word, container, false);
        ButterKnife.bind(this, v);

        UserLangAdapter langAdapter = new UserLangAdapter();
        mLangSpinner.setAdapter(langAdapter);
        mLangSpinner.setOnItemSelectedListener(this);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                mAddButton.setEnabled(validation());
            }
        };
        mNameEdit.addTextChangedListener(textWatcher);

        mAddButton.setEnabled(false);

        mTranslationRecycler.setHasFixedSize(true);
        mTranslationRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        mWord = getArguments().getParcelable(WORD_PARAM);
        mLang = getArguments().getParcelable(LANG_PARAM);

        if(mWord != null) {
            mLang = mWord.lang;

            mNameEdit.setText(mWord.text);
            mSubNameEdit.setText(mWord.subText);
            mDescEdit.setText(mWord.desc);
            mAddButton.setText(R.string.button_update);
        }

        mLangSpinner.setSelection(langAdapter.getPosition(mLang.getId()));

        setMode();

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TableWordListener) {
            mListener = (TableWordListener) context;
        }
    }

    @OnClick(R.id.add_btn)
    public void onAddButtonClicked() {
        if (mWord != null) {
            mWord.text = mNameEdit.getText().toString();
            mWord.subText = mSubNameEdit.getText().toString();
            mWord.desc = mDescEdit.getText().toString();
            mWord.lang = mLang;
            mWord.save();
            if(mListener != null) {
                mListener.onTableWordEvent(DataEventType.CREATE, mWord);
            }
        } else {
            mWord = new Word(mLang, mNameEdit.getText().toString(),
                    mSubNameEdit.getText().toString(),
                    mDescEdit.getText().toString());
            mWord.save();
            if(mListener != null) {
                mListener.onTableWordEvent(DataEventType.UPDATE, mWord);
            }
        }

        setMode();
    }

    @OnClick(R.id.add_translation_btn)
    public void onAddTranslationButtonClicked() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag(TAG_ADD_TRANSLATION_DIALOG);
        if (prev != null) {
            ft.remove(prev);
        }

        DialogFragment newFragment = AddTranslationDialogFragment.newInstance(mWord);
        newFragment.show(ft, TAG_ADD_TRANSLATION_DIALOG);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private boolean validation() {
        String name = mNameEdit.getText().toString();

        if(name.length() > 0) {
            if(mWord != null) {
                return (Word.count(Word.class, "text = ? AND id != ?", new String[] {name, mWord.getId().toString()}, null, null, null) == 0);
            }
            else {
                return (Word.count(Word.class, "text = ?", new String[] {name}, null, null, null) == 0);
            }
/*


            List<Word> words = Word.find(Word.class, "text = ?", name);

            if(mWord != null) {
                return (words.size() == 0) || (words.size() == 1 && words.get(0).getId() == mWord.getId());
            }
            else {
                return words.size() == 0;
            }*/
        }

        return false;
    }

    private void setMode() {
        if(mWord != null) {
            mAddButton.setText(R.string.button_update);
            mTranslationsLayout.setVisibility(View.VISIBLE);
            mTranslationAdapter = new TranslationRecyclerViewAdapter(mWord, this);
            mTranslationRecycler.setAdapter(mTranslationAdapter);
        }
        else {
            mAddButton.setText(R.string.button_add);
            mTranslationsLayout.setVisibility(View.GONE);
        }
    }

    public void refreshTranslations() {
        mTranslationAdapter.updateTranslations();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mLang = (Lang) parent.getSelectedItem();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onLongClick(Translation item) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag(TAG_DELETE_TRANSLATION_DIALOG);
        if (prev != null) {
            ft.remove(prev);
        }

        DialogFragment newFragment = DeleteTranslationDialogFragment.newInstance(item);
        newFragment.show(ft, TAG_DELETE_TRANSLATION_DIALOG);
    }
}
