package net.or3lll.languagelearning.configuration.word.edit.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;

import net.or3lll.languagelearning.R;
import net.or3lll.languagelearning.configuration.word.edit.WordSearchAdapter;
import net.or3lll.languagelearning.configuration.word.edit.TableTranslationListener;
import net.or3lll.languagelearning.data.DataEventType;
import net.or3lll.languagelearning.data.Translation;
import net.or3lll.languagelearning.data.Word;

/**
 * Created by X2014568 on 04/03/2016.
 */
public class AddTranslationDialogFragment extends DialogFragment {

    public static String WORD_PARAM = "WORD";

    private TableTranslationListener mListener;

    private WordSearchAdapter searchAdapter;

    public static AddTranslationDialogFragment newInstance(Word word) {
        AddTranslationDialogFragment fragment = new AddTranslationDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(WORD_PARAM, word);
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

        final Word word = getArguments().getParcelable(WORD_PARAM);
        searchAdapter = new WordSearchAdapter(word.getId(), word.lang.getId());
        searchAutoComplete.setAdapter(searchAdapter);

        searchAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            Word selectedWord = (Word) parent.getItemAtPosition(position);
            Translation translation = new Translation(word, selectedWord);
            translation.save();
            if(mListener != null) {
                mListener.onTableTranslationEvent(DataEventType.CREATE, translation);
            }
            dismiss();
        });

        builder.setTitle(R.string.word_dialog_title_add_translation)
            .setView(contentView)
            .setNegativeButton(R.string.all_cancel, null);

        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if(activity instanceof TableTranslationListener) {
            mListener = (TableTranslationListener) activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
