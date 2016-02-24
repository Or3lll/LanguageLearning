package net.or3lll.languagelearning.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import net.or3lll.languagelearning.R;
import net.or3lll.languagelearning.configuration.lang.LangListActivity;

/**
 * Created by or3lll on 24/02/2016.
 */
public class LangMinimumAdviceDialogFragment extends DialogFragment {

    public static LangMinimumAdviceDialogFragment newInstance() {
        LangMinimumAdviceDialogFragment fragment = new LangMinimumAdviceDialogFragment();
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.message_dialog_advice_minimum_lang)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent testIntent = new Intent(getActivity(), LangListActivity.class);
                        startActivity(testIntent);
                    }
                })
                .setNegativeButton(R.string.no, null);
        return builder.create();
    }
}
