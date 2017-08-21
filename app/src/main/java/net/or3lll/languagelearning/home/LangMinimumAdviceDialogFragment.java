package net.or3lll.languagelearning.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;

import net.or3lll.languagelearning.R;
import net.or3lll.languagelearning.configuration.importer.DataImporter;
import net.or3lll.languagelearning.configuration.lang.list.LangListActivity;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
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
        builder.setMessage(R.string.message_dialog_advice_minimum_lang)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    Intent testIntent = new Intent(getActivity(), LangListActivity.class);
                    startActivity(testIntent);
                })
                // TODO: to move after settings page will be created
                .setNeutralButton("Init", (dialog, which) -> {
                    String initJson = "{\n" +
                            "\t\"langs\": [{\n" +
                            "\t\t\"isoCode\": \"fr_FR\",\n" +
                            "\t\t\"name\": \"Français\"\n" +
                            "\t}, {\n" +
                            "\t\t\"isoCode\": \"ja_JP\",\n" +
                            "\t\t\"name\": \"Japonais\"\n" +
                            "\t}],\n" +
                            "\t\"words\": [{\n" +
                            "\t\t\"isoCode\": \"fr_FR\",\n" +
                            "\t\t\"text\": \"je\",\n" +
                            "\t\t\"subText\": \"\",\n" +
                            "\t\t\"desc\": \"\"\n" +
                            "\t}, {\n" +
                            "\t\t\"isoCode\": \"ja_JP\",\n" +
                            "\t\t\"text\": \"わたし\",\n" +
                            "\t\t\"subText\": \"\",\n" +
                            "\t\t\"desc\": \"\"\n" +
                            "\t}, {\n" +
                            "\t\t\"isoCode\": \"fr_FR\",\n" +
                            "\t\t\"text\": \"moi\",\n" +
                            "\t\t\"subText\": \"\",\n" +
                            "\t\t\"desc\": \"\"\n" +
                            "\t}, {\n" +
                            "\t\t\"isoCode\": \"fr_FR\",\n" +
                            "\t\t\"text\": \"nous\",\n" +
                            "\t\t\"subText\": \"\",\n" +
                            "\t\t\"desc\": \"\"\n" +
                            "\t}, {\n" +
                            "\t\t\"isoCode\": \"ja_JP\",\n" +
                            "\t\t\"text\": \"わたしたち\",\n" +
                            "\t\t\"subText\": \"\",\n" +
                            "\t\t\"desc\": \"\"\n" +
                            "\t}],\n" +
                            "\t\"translations\": [{\n" +
                            "\t\t\"isoCode1\": \"je\",\n" +
                            "\t\t\"text1\": \"fr_FR\",\n" +
                            "\t\t\"isoCode2\": \"わたし\",\n" +
                            "\t\t\"text2\": \"ja_JP\"\n" +
                            "\t}, {\n" +
                            "\t\t\"isoCode1\": \"ja_JP\",\n" +
                            "\t\t\"text1\": \"わたし\",\n" +
                            "\t\t\"isoCode2\": \"fr_FR\",\n" +
                            "\t\t\"text2\": \"moi\"\n" +
                            "\t}, {\n" +
                            "\t\t\"isoCode1\": \"fr_FR\",\n" +
                            "\t\t\"text1\": \"nous\",\n" +
                            "\t\t\"isoCode2\": \"ja_JP\",\n" +
                            "\t\t\"text2\": \"わたしたち\"\n" +
                            "\t}]\n" +
                            "}";

                    DataImporter importer = new DataImporter();
                    importer.load(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(initJson.getBytes()))));
                    importer.apply();
                })
                .setNegativeButton(R.string.no, null);
        return builder.create();
    }
}
