package net.or3lll.languagelearning.configuration.word.list;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import net.or3lll.languagelearning.R;
import net.or3lll.languagelearning.data.DataEventType;
import net.or3lll.languagelearning.data.Translation;
import net.or3lll.languagelearning.data.Word;

/**
 * Created by Or3lll on 03/03/2016.
 */
public class DeleteWordDialogFragment extends DialogFragment {
    public static String WORD_PARAM = "WORD";

    private TableWordListener mListener;

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
        builder.setMessage(R.string.word_dialog_message_delete)
                .setPositiveButton(R.string.all_yes, (dialog, which) -> {
                    Word word = getArguments().getParcelable(WORD_PARAM);
                    if (word != null) {
                        String sWordId = word.getId().toString();
                        for (Translation translation :
                                Translation.find(Translation.class, "word1 = ? OR word2 = ?", new String[]{sWordId, sWordId})) {
                            translation.delete();
                        }
                        word.delete();
                        if (mListener != null) {
                            mListener.onTableWordEvent(DataEventType.DELETE, word);
                        }
                    }
                })
                .setNegativeButton(R.string.all_no, null);

        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if(activity instanceof TableWordListener) {
            mListener = (TableWordListener) activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
