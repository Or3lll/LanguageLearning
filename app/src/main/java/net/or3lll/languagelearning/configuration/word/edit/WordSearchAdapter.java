package net.or3lll.languagelearning.configuration.word.edit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import net.or3lll.languagelearning.R;
import net.or3lll.languagelearning.data.Word;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by or3lll on 04/03/2016.
 */
public class WordSearchAdapter extends BaseAdapter implements Filterable {
    private long mWordId;
    private long mLangId;
    private List<Word> mOriginalWords;
    private List<Word> mWords;

    public WordSearchAdapter(long wordId, long langId) {
        mWordId = wordId;
        mLangId = langId;
        String sWordId = Long.toString(mWordId);
        mOriginalWords = Word.findWithQuery(Word.class, "SELECT w.id, w.lang, w.text, w.sub_text, w.desc " +
                        "FROM Word w " +
                        "LEFT JOIN Translation t1 ON w.id = t1.word1 " +
                        "LEFT JOIN Translation t2 ON w.id = t2.word2 " +
                        "WHERE w.lang != ? " +
                        "AND (t1.id IS NULL OR t1.word2 != ?) " +
                        "AND (t2.id IS NULL OR t2.word1 != ?) "
                        , new String[] {Long.toString(mLangId), sWordId, sWordId});

        mWords = new ArrayList<>(mOriginalWords);
    }

    @Override
    public int getCount() {
        return mWords.size();
    }

    @Override
    public Object getItem(int position) {
        return mWords.get(position);
    }

    @Override
    public long getItemId(int position) {
        Word w = mWords.get(position);
        if(w != null) {
            return 0;
        }

        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;

        if(convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_word, null);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        }
        else {
            holder = (Holder) convertView.getTag();
        }

        Word word = mWords.get(position);
        if(word != null) {
            holder.getWordName().setText(word.text);

            if(word.lang != null) {
                holder.getLangName().setText(word.lang.getName());
            }
        }

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                if(constraint == null || constraint.length() == 0) {
                    results.values = mOriginalWords;
                    results.count = mOriginalWords.size();
                }
                else {
                    List<Word> words = new ArrayList<>();
                    for (Word w :
                            mOriginalWords) {
                        if (w.text.toUpperCase().startsWith(constraint.toString().toUpperCase())) {
                            words.add(w);
                        }
                    }

                    results.values = words;
                    results.count = words.size();
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mWords = (List<Word>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    private class Holder {
        private View mItemView;
        private TextView mWordNameTextView;
        private TextView mLangNameTextView;

        public Holder(View itemView) {
            mItemView = itemView;
        }

        public TextView getWordName() {
            if(mWordNameTextView == null) {
                mWordNameTextView = (TextView) mItemView.findViewById(R.id.word_textview);
            }

            return mWordNameTextView;
        }

        public TextView getLangName() {
            if(mLangNameTextView == null) {
                mLangNameTextView = (TextView) mItemView.findViewById(R.id.lang_textview);
            }

            return mLangNameTextView;
        }
    }
}
