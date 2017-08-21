package net.or3lll.languagelearning.configuration.word.edit;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.or3lll.languagelearning.R;
import net.or3lll.languagelearning.data.Translation;
import net.or3lll.languagelearning.data.Word;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by X2014568 on 08/03/2016.
 */
public class TranslationRecyclerViewAdapter extends RecyclerView.Adapter<TranslationRecyclerViewAdapter.ViewHolder> {

    private Word mWord;
    private SparseArray<Translation> mValues;
    private int mValuesNumber;
    private OnClickListener mListener;

    public TranslationRecyclerViewAdapter(Word word, OnClickListener listener) {
        mWord = word;
        mListener = listener;

        setTranslations();
    }

    private void setTranslations() {
        String sWordId = mWord.getId().toString();
        mValuesNumber = (int) Translation.count(Translation.class, "word1 = ? OR word2 = ? ", new String[] {sWordId, sWordId});
        mValues = new SparseArray<>(mValuesNumber);
    }

    public void updateTranslations() {
        setTranslations();
        notifyDataSetChanged();
    }

    @Override
    public TranslationRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_word, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Translation translation = mValues.get(position);
        if(translation == null) {
            String sWordID = mWord.getId().toString();
            translation = Translation.find(Translation.class, "word1 = ? OR word2 = ? ", new String[] {sWordID, sWordID}, null, null, position + ", 1").get(0);
            mValues.append(position, translation);
        }
        holder.mItem = translation;

        if(translation.word1.getId() == mWord.getId()) {
            holder.mWordView.setText(translation.word2.text);
            if(translation.word2.lang != null) {
                holder.mLangView.setText(translation.word2.lang.getName());
            }
        }
        else {
            holder.mWordView.setText(translation.word1.text);
            if(translation.word1.lang != null) {
                holder.mLangView.setText(translation.word1.lang.getName());
            }
        }

        holder.mView.setOnLongClickListener(v -> {
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
        public final View mView;
        @BindView(R.id.word_textview) TextView mWordView;
        @BindView(R.id.lang_textview) TextView mLangView;
        public Translation mItem;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mView = view;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mWordView.getText() + "'" + " '" + mLangView.getText() + "'";
        }
    }

    public interface OnClickListener {
        void onLongClick(Translation item);
    }
}
