package net.or3lll.languagelearning.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;

import net.or3lll.languagelearning.R;
import net.or3lll.languagelearning.configuration.importer.DataImporter;
import net.or3lll.languagelearning.configuration.lang.list.LangListActivity;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
        builder.setMessage(R.string.lang_dialog_message_advice_minimum)
                .setPositiveButton(R.string.all_yes, (dialog, which) -> {
                    Intent testIntent = new Intent(getActivity(), LangListActivity.class);
                    startActivity(testIntent);
                })
                .setNeutralButton("Init", (dialog, which) -> {
                    AssetManager assetManager = getContext().getAssets();
                    try {
                        InputStream initInputStream = assetManager.open("init.json");
                        DataImporter importer = new DataImporter();
                        importer.load(new BufferedReader(new InputStreamReader(initInputStream)));
                        importer.apply();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                })
                .setNegativeButton(R.string.all_no, null);
        return builder.create();
    }
}
