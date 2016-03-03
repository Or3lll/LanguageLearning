package net.or3lll.languagelearning.configuration.word;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import net.or3lll.languagelearning.data.Lang;
import net.or3lll.languagelearning.data.Word;

import java.util.List;

/**
 * Created by X2014568 on 03/03/2016.
 */
public class EditWordFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    public static String WORD_ID_PARAM = "WORD_ID";
    public static String LANG_ID_PARAM = "LANG_ID";

    private Spinner mLangSpinner;
    private EditText mNameEdit;
    private Button mAddButton;

    private OnFragmentInteractionListener mListener;

    private Word mWord;
    private Lang mLang;


    public static EditWordFragment newInstance(long wordId, long langId) {
        EditWordFragment fragment = new EditWordFragment();
        Bundle args = new Bundle();
        args.putLong(WORD_ID_PARAM, wordId);
        args.putLong(LANG_ID_PARAM, langId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_word, container, false);

        mLangSpinner = (Spinner) v.findViewById(R.id.langSpinner);
        UserLangAdapter langAdapter = new UserLangAdapter();
        mLangSpinner.setAdapter(langAdapter);
        mLangSpinner.setOnItemSelectedListener(this);

        mNameEdit = (EditText) v.findViewById(R.id.name_edit);
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

        mAddButton = (Button) v.findViewById(R.id.add_btn);
        mAddButton.setEnabled(false);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWord != null) {
                    mWord.text = mNameEdit.getText().toString();
                    mWord.lang = mLang;
                    mWord.save();
                    mListener.onWordAdded();
                } else {
                    Word w = new Word(mLang, mNameEdit.getText().toString(), "", "");
                    w.save();
                    mListener.onWordUpdated();
                }
            }
        });

        long wordId = getArguments().getLong(WORD_ID_PARAM, -1);
        long langId = getArguments().getLong(LANG_ID_PARAM, -1);

        mWord = Word.findById(Word.class, wordId);
        if(mWord != null) {
            mLang = Lang.findById(Lang.class, mWord.getId());

            mNameEdit.setText(mWord.text);
            mAddButton.setText(R.string.button_update);

            mAddButton.setEnabled(validation());
        } else {
            mLang = Lang.findById(Lang.class, langId);
        }

        mLangSpinner.setSelection(langAdapter.getPosition(mLang.getId()));

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private boolean validation() {
        String name = mNameEdit.getText().toString();

        if(name.length() > 0) {
            List<Word> words = Word.find(Word.class, "text = ?", name);

            if(mWord != null) {
                return (words.size() == 0) || (words.size() == 1 && words.get(0).getId() == mWord.getId());
            }
            else {
                return words.size() == 0;
            }
        }

        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface OnFragmentInteractionListener {
        void onWordAdded();
        void onWordUpdated();
    }
}
