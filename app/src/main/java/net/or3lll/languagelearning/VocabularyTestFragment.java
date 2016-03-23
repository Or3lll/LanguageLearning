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


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VocabularyTestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VocabularyTestFragment extends Fragment {

    private TextView textTextView;
    private TextView subTextTextView;
    private EditText answerEditText;
    private Button displaySubTextBtn;
    private Button checkBtn;

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
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_vocabulary_test, container, false);

        textTextView = (TextView) v.findViewById(R.id.text_text);
        subTextTextView = (TextView) v.findViewById(R.id.subtext_text);
        answerEditText = (EditText) v.findViewById(R.id.answer_edittext);

        displaySubTextBtn = (Button) v.findViewById(R.id.display_subtext_btn);
        checkBtn = (Button) v.findViewById(R.id.check_btn);

        displaySubTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subTextTextView.setVisibility(View.VISIBLE);
                return;
            }
        });

        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });

        setWords();

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("score", score);
        outState.putInt("tent", attempts);
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
        subTextTextView.setText(mWord.subText);
        answerEditText.setText("");

        subTextTextView.setVisibility(View.INVISIBLE);
    }
}
