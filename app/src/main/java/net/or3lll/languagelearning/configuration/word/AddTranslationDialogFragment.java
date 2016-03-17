package net.or3lll.languagelearning.configuration.word;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import net.or3lll.languagelearning.R;
import net.or3lll.languagelearning.data.Lang;
import net.or3lll.languagelearning.data.Translation;
import net.or3lll.languagelearning.data.Word;

/**
 * Created by X2014568 on 04/03/2016.
 */
public class AddTranslationDialogFragment extends DialogFragment {

    public static String WORD_ID_PARAM = "WORD_ID";

    private OnAddTranslationListener mListener;

    public static AddTranslationDialogFragment newInstance(long wordId) {
        AddTranslationDialogFragment fragment = new AddTranslationDialogFragment();
        Bundle args = new Bundle();
        args.putLong(WORD_ID_PARAM, wordId);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View contentView = inflater.inflate(R.layout.dialog_add_translation, null);
        AutoCompleteTextView searchAutoComplete = (AutoCompleteTextView) contentView.findViewById(R.id.searchAutoComplete);

        long wordId = getArguments().getLong(WORD_ID_PARAM);
        final Word word = Word.findById(Word.class, wordId);
        WordSearchAdapter searchAdapter = new WordSearchAdapter(word.getId(), word.lang.getId());
        searchAutoComplete.setAdapter(searchAdapter);

        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word w = (Word) parent.getItemAtPosition(position);
                Translation t = new Translation(word, w);
                t.save();
                if(mListener != null) {
                    mListener.onTranslationAdd();
                }
                dismiss();
            }
        });

        builder.setTitle(R.string.title_dialog_add_translation)
            .setView(contentView)
            .setNegativeButton(R.string.cancel, null);

        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if(activity instanceof OnAddTranslationListener) {
            mListener = (OnAddTranslationListener) activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnAddTranslationListener {
        void onTranslationAdd();
    }
}
