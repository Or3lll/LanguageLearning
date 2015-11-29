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

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VocabularyTestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VocabularyTestFragment extends Fragment {

    public class JpFrWord {
        public String kanji;
        public String kana;
        public String french;

        public JpFrWord(String _kanji, String _kana, String _french) {
            this.kanji = _kanji;
            this.kana = _kana;
            this.french = _french;
        }
    }

    private List<JpFrWord> words;

    private TextView kanjiTextView;
    private TextView kanaTextView;
    private EditText frEditText;
    private Button displayKanaBtn;
    private Button checkBtn;

    private int index;
    private int score;
    private int attempts;

    public VocabularyTestFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment VocabularyTestFragment.
     */
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

        words = new ArrayList<JpFrWord>();

        words.add(new JpFrWord("教師", "", "Enseignant"));
        words.add(new JpFrWord("私", "わたし", "Je"));
        words.add(new JpFrWord("私達", "わたしたち", "Nous"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_vocabulary_test, container, false);

        kanjiTextView = (TextView) v.findViewById(R.id.kanji_text);
        kanaTextView = (TextView) v.findViewById(R.id.kana_text);
        frEditText = (EditText) v.findViewById(R.id.french_response);

        displayKanaBtn = (Button) v.findViewById(R.id.display_kana_btn);
        checkBtn = (Button) v.findViewById(R.id.check_btn);

        displayKanaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kanaTextView.setVisibility(View.VISIBLE);
                return;
            }
        });

        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JpFrWord word = words.get(index);
                attempts++;

                if (word.french.equalsIgnoreCase(frEditText.getText().toString())) {
                    score++;
                    Toast.makeText(VocabularyTestFragment.this.getContext(), "Correct " + score + " / " + attempts, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(VocabularyTestFragment.this.getContext(), "Faux " + score + " / " + attempts, Toast.LENGTH_SHORT).show();
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
        index = (int) (Math.random() * (double)words.size());

        JpFrWord word = words.get(index);

        kanjiTextView.setText(word.kanji);
        kanaTextView.setText(word.kana);
        frEditText.setText("");

        kanaTextView.setVisibility(View.INVISIBLE);
    }
}
