package net.or3lll.languagelearning.configuration.word;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import net.or3lll.languagelearning.R;
import net.or3lll.languagelearning.data.Translation;
import net.or3lll.languagelearning.data.Word;

/**
 * Created by Or3lll on 03/03/2016.
 */
public class DeleteWordDialogFragment extends DialogFragment {
    public static String WORD_PARAM = "WORD";

    private OnDeleteWordListener mListener;

    public static DeleteWordDialogFragment newInstance(Word word) {
        DeleteWordDialogFragment fragment = new DeleteWordDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(WORD_PARAM, word);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.message_dialog_delete_word)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Word word = getArguments().getParcelable(WORD_PARAM);
                        if (word != null) {
                            for (Translation translation :
                                    Translation.listAll(Translation.class)) {
                                if(translation.word1.getId() == word.getId()
                                        || translation.word2.getId() == word.getId()) {
                                    translation.delete();
                                }

                            }
                            word.delete();
                            if (mListener != null) {
                                mListener.onWordDeleted(word);
                            }
                        }
                    }
                })
                .setNegativeButton(R.string.no, null);

        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if(activity instanceof OnDeleteWordListener) {
            mListener = (OnDeleteWordListener) activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnDeleteWordListener {
        void onWordDeleted(Word word);
    }
}
