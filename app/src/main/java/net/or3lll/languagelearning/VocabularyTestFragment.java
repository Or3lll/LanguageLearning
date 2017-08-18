package net.or3lll.languagelearning;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    @BindView(R.id.answer_edittext) private EditText answerEditText;
    @BindView(R.id.display_subtext_btn) Button displaySubTextBtn;

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
        List<Translation> translations = Translation.listAll(Translation.class);
        int index = (int) (Math.random() * (double)Translation.count(Translation.class));
        Translation translation = Translation.find(Translation.class, null, null, null, null, index + ", 1").get(0);

        if(attempts % 2 == 0) {
            mWord = translation.word1;
        }
        else {
            mWord = translation.word2;
        }

        textTextView.setText(mWord.text);
        if(mWord.subText != null && !mWord.subText.isEmpty()) {
            subTextTextView.setText(mWord.subText);
            displaySubTextBtn.setVisibility(View.VISIBLE);
            subTextTextView.setVisibility(View.INVISIBLE);
        }
        else {
            displaySubTextBtn.setVisibility(View.GONE);
            subTextTextView.setVisibility(View.GONE);
        }
        answerEditText.setText("");


    }
}
