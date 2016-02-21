package net.or3lll.languagelearning.configuration.lang;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import net.or3lll.languagelearning.R;
import net.or3lll.languagelearning.data.Lang;

/**
 * Created by Or3lll on 21/02/2016.
 */
public class DeleteLangDialogFragment extends DialogFragment{
    public static String LANG_ID_PARAM = "LANG_ID";

    private OnDeleteLangListener mListener;

    public static DeleteLangDialogFragment newInstance(long langId) {
        DeleteLangDialogFragment fragment = new DeleteLangDialogFragment();
        Bundle args = new Bundle();
        args.putLong(LANG_ID_PARAM, langId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.message_dialog_delete_lang)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Lang lang = Lang.findById(Lang.class, getArguments().getLong(LANG_ID_PARAM, -1));
                        if (lang != null) {
                            lang.delete();
                            if (mListener != null) {
                                mListener.onLangDeleted(lang);
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

        if(activity instanceof OnDeleteLangListener) {
            mListener = (OnDeleteLangListener) activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnDeleteLangListener {
        void onLangDeleted(Lang lang);
    }
}
