package net.or3lll.languagelearning.test;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.or3lll.languagelearning.R;
import net.or3lll.languagelearning.data.Lang;
import net.or3lll.languagelearning.data.Translation;
import net.or3lll.languagelearning.data.Word;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VocabularyTestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VocabularyTestFragment extends Fragment {

    private Unbinder unbinder;

    @BindView(R.id.text_text) TextView textTextView;
    @BindView(R.id.subtext_text) TextView subTextTextView;
    @BindView(R.id.answer_edittext) EditText answerEditText;
    @BindView(R.id.display_subtext_btn) Button displaySubTextBtn;

    private Lang langSrc;
    private Lang langDst;

    private Word mWord;

    private int score;
    private int attempts;

    public VocabularyTestFragment() {
    }

    public static VocabularyTestFragment newInstance() {
        VocabularyTestFragment fragment = new VocabularyTestFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if(savedInstanceState != null) {
                score = savedInstanceState.getInt("score");
                attempts = savedInstanceState.getInt("tent");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_vocabulary_test, container, false);
        unbinder = ButterKnife.bind(this, v);

        setWords();

        return v;
    }

    public void setLangs(Lang langSrc, Lang langDst) {
        this.langSrc = langSrc;
        this.langDst = langDst;

        setWords();
    }

    @OnClick(R.id.display_subtext_btn)
    public void onSubtextButtonClicked() {
        subTextTextView.setVisibility(View.VISIBLE);
        return;
    }

    @OnClick(R.id.check_btn)
    public void onCheckButtonClicked() {
        boolean hasCorrectResult = false;
        attempts++;

        for (Translation translation :
                Translation.listAll(Translation.class)) {
            if (translation.word1.getId() == mWord.getId()) {
                if(translation.word2.text.equalsIgnoreCase(answerEditText.getText().toString())) {
                    hasCorrectResult = true;
                }
            }

            else if (translation.word2.getId() == mWord.getId()) {
                if(translation.word1.text.equalsIgnoreCase(answerEditText.getText().toString())) {
                    hasCorrectResult = true;
                }
            }
        }

        if(hasCorrectResult) {
            score++;
            Toast.makeText(VocabularyTestFragment.this.getContext(), String.format(getString(R.string.good_answer), score, attempts), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(VocabularyTestFragment.this.getContext(), String.format(getString(R.string.bad_answer), score, attempts), Toast.LENGTH_SHORT).show();
        }

        setWords();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("score", score);
        outState.putInt("tent", attempts);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setWords() {
        if (langSrc != null && langDst != null) {

            List<Translation> translations = Translation.findWithQuery(Translation.class,
                    "SELECT * " +
                            "FROM Translation t " +
                            "LEFT JOIN Word w1 ON t.word1 = w1.id " +
                            "LEFT JOIN Word w2 ON t.word2 = w2.id " +
                            "WHERE (w1.lang = ? OR w2.lang = ?) " +
                            "   AND (w1.lang = ? OR w2.lang = ?) "
                    , new String[] {langSrc.getId().toString(), langSrc.getId().toString(),
                            langDst.getId().toString(), langDst.getId().toString()});

            int index = (int) (Math.random() * (double) translations.size());
            Translation translation = translations.get(index);

            if (translation.word1.lang.getId() == langSrc.getId()) {
                mWord = translation.word1;
            } else {
                mWord = translation.word2;
            }

            textTextView.setText(mWord.text);
            if (mWord.subText != null && !mWord.subText.isEmpty()) {
                subTextTextView.setText(mWord.subText);
                displaySubTextBtn.setVisibility(View.VISIBLE);
                subTextTextView.setVisibility(View.INVISIBLE);
            } else {
                displaySubTextBtn.setVisibility(View.GONE);
                subTextTextView.setVisibility(View.GONE);
            }
            answerEditText.setText("");
        }
    }
}
