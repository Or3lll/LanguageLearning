package net.or3lll.languagelearning.configuration.word.list;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.or3lll.languagelearning.R;
import net.or3lll.languagelearning.data.Lang;
import net.or3lll.languagelearning.data.Word;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by or3lll on 23/02/2016.
 */
public class WordRecyclerViewAdapter extends RecyclerView.Adapter<WordRecyclerViewAdapter.ViewHolder> {

    private Lang mLang;
    private SparseArray<Word> mValues;
    private int mValuesNumber;
    private OnClickListener mListener;

    public WordRecyclerViewAdapter(Lang lang, OnClickListener listener) {
        mLang = lang;
        mListener = listener;

        setWords();
    }

    private void setWords() {
        if (mLang != null) {
            mValuesNumber = (int) Word.count(Word.class, "lang = ?", new String[]{mLang.getId().toString()});
            mValues = new SparseArray<>(mValuesNumber);
        }
    }

    public void updateWords() {
        setWords();
        notifyDataSetChanged();
    }

    public void updateLang(Lang lang) {
        mLang = lang;
        updateWords();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_word_simple, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Word word = mValues.get(position);
        if(word == null) {
            word = Word.find(Word.class, "lang = ?", new String[] {mLang.getId().toString()}, null, "text", position + ", 1").get(0);
            mValues.append(position, word);
        }
        holder.mItem = word;

        holder.mContentView.setText(mValues.get(position).text);

        holder.mContentView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onClick(holder.mItem);
            }
        });

        holder.mEdit.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onEditClick(holder.mItem);
            }
        });

        holder.mContentView.setOnLongClickListener(v -> {
            if(mListener != null) {
                mListener.onLongClick(holder.mItem);
                return true;
            }

            return false;
        });
    }

    @Override
    public int getItemCount() {
        return mValuesNumber;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.content) TextView mContentView;
        @BindView(R.id.edit) ImageView mEdit;

        public Word mItem;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    public interface OnClickListener {
        void onClick(Word item);
        void onEditClick(Word item);
        void onLongClick(Word item);
    }
}
