package net.or3lll.languagelearning.configuration.word;

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
import net.or3lll.languagelearning.data.Lang;

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
