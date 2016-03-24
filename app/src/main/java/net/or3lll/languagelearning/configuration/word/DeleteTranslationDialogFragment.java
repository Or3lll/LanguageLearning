package net.or3lll.languagelearning.configuration.word;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import net.or3lll.languagelearning.R;
import net.or3lll.languagelearning.data.DataEventType;
import net.or3lll.languagelearning.data.Translation;

/**
 * Created by or3lll on 08/03/2016.
 */
public class DeleteTranslationDialogFragment extends DialogFragment {
    public static String TRANSLATION_PARAM = "TRANSLATION";

    private TableTranslationListener mListener;

    public static DeleteTranslationDialogFragment newInstance(Translation translation) {
        DeleteTranslationDialogFragment fragment = new DeleteTranslationDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(TRANSLATION_PARAM, translation);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.message_dialog_delete_translation)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Translation translation = getArguments().getParcelable(TRANSLATION_PARAM);
                        if (translation != null) {
                            translation.delete();
                            if (mListener != null) {
                                mListener.onTableTranslationEvent(DataEventType.DELETE, translation);
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

        if (activity instanceof TableTranslationListener) {
            mListener = (TableTranslationListener) activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
