package net.or3lll.languagelearning.configuration.lang;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import net.or3lll.languagelearning.R;
import net.or3lll.languagelearning.configuration.shared.AvailableDefaultLangAdapter;
import net.or3lll.languagelearning.data.Lang;

/**
 * Created by or3lll on 24/02/2016.
 */
public class DefaultLangsDialogFragment extends DialogFragment {

    private OnDefaultLangSelected mListener;

    public static DefaultLangsDialogFragment newInstance() {
        DefaultLangsDialogFragment fragment = new DefaultLangsDialogFragment();
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.title_dialog_add_lang)
                // TODO gérer dans l'adapter l'absence de langue
                .setAdapter(new AvailableDefaultLangAdapter(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Lang lang = (Lang) ((AlertDialog) dialog).getListView().getAdapter().getItem(which);
                        lang.save();

                        if (mListener != null) {
                            mListener.onLangSelected(lang);
                        }
                    }
                })
                .setNeutralButton(R.string.button_dialog_add_lang, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mListener != null) {
                            mListener.onOtherSelected();
                        }
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if(activity instanceof OnDefaultLangSelected) {
            mListener = (OnDefaultLangSelected) activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnDefaultLangSelected {
        void onLangSelected(Lang lang);
        void onOtherSelected();
    }
}
