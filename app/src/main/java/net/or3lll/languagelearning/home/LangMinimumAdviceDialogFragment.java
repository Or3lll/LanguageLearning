package net.or3lll.languagelearning.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import net.or3lll.languagelearning.R;
import net.or3lll.languagelearning.configuration.lang.LangListActivity;
import net.or3lll.languagelearning.data.Lang;
import net.or3lll.languagelearning.data.Translation;
import net.or3lll.languagelearning.data.Word;

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
                // TODO: to move after settings page will be created
                .setNeutralButton("Init", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Lang frLang = new Lang("Français", "fr_FR");
                        frLang.save();
                        Lang jnLang = new Lang("Japonais", "jn_JP");
                        jnLang.save();

                        Word w1 = new Word(frLang, "je", "", "");
                        w1.save();
                        Word w2 = new Word(jnLang, "わたし", "", "");
                        w2.save();
                        Translation t = new Translation(w1, w2);
                        t.save();
                        Word w3 = new Word(frLang, "moi", "", "");
                        w3.save();
                        t = new Translation(w2, w3);
                        t.save();

                        w1 = new Word(frLang, "nous", "", "");
                        w1.save();
                        w2 = new Word(jnLang, "わたしたち", "", "");
                        w2.save();
                        t = new Translation(w1, w2);
                        t.save();

                        w1 = new Word(frLang, "vous", "", "singulier");
                        w1.save();
                        w2 = new Word(jnLang, "あなた", "", "");
                        w2.save();
                        t = new Translation(w1, w2);
                        t.save();

                        w1 = new Word(frLang, "professeur", "", "");
                        w1.save();
                        w2 = new Word(jnLang, "せんせい", "", "");
                        w2.save();
                        t = new Translation(w1, w2);
                        t.save();
                        w3 = new Word(frLang, "instituteur", "", "");
                        w3.save();
                        t = new Translation(w2, w3);
                        t.save();

                        w1 = new Word(frLang, "ingénieur", "", "");
                        w1.save();
                        w2 = new Word(jnLang, "エンジニア", "", "");
                        w2.save();
                        t = new Translation(w1, w2);
                        t.save();

                        w1 = new Word(frLang, "oui", "", "");
                        w1.save();
                        w2 = new Word(jnLang, "はい", "", "");
                        w2.save();
                        t = new Translation(w1, w2);
                        t.save();

                        w1 = new Word(frLang, "non", "", "");
                        w1.save();
                        w2 = new Word(jnLang, "いいえ", "", "");
                        w2.save();
                        t = new Translation(w1, w2);
                        t.save();
                    }
                })
                .setNegativeButton(R.string.no, null);
        return builder.create();
    }
}
