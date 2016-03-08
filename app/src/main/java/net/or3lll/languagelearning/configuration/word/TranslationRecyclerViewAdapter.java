package net.or3lll.languagelearning.configuration.word;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.or3lll.languagelearning.R;
import net.or3lll.languagelearning.data.Lang;
import net.or3lll.languagelearning.data.Translation;
import net.or3lll.languagelearning.data.Word;

import java.util.List;

/**
 * Created by X2014568 on 08/03/2016.
 */
public class TranslationRecyclerViewAdapter extends RecyclerView.Adapter<TranslationRecyclerViewAdapter.ViewHolder> {

    private Word mWord;
    private List<Translation> mTranslations;
    private OnClickListener mListener;

    public TranslationRecyclerViewAdapter(Word word, List<Translation> translations, OnClickListener listener) {
        mWord = word;
        mTranslations = translations;
        mListener = listener;
    }

    public void setTranslations(List<Translation> translations) {
        mTranslations = translations;
        notifyDataSetChanged();
    }

    @Override
    public TranslationRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_word, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Translation translation = mTranslations.get(position);
        holder.mItem = translation;
        if(translation.word1.getId() == mWord.getId()) {
            holder.mWordView.setText(translation.word2.text);
            Lang lang = Lang.findById(Lang.class, translation.word2.getId());
            if(lang != null) {
                holder.mLangView.setText(lang.name);
            }
        }
        else {
            holder.mWordView.setText(translation.word1.text);
            Lang lang = Lang.findById(Lang.class, translation.word1.getId());
            if(lang != null) {
                holder.mLangView.setText(lang.name);
            }
        }

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(mListener != null) {
                    mListener.onLongClick(holder.mItem);
                    return true;
                }

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTranslations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mWordView;
        public final TextView mLangView;
        public Translation mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mWordView = (TextView) view.findViewById(R.id.word_textview);
            mLangView = (TextView) view.findViewById(R.id.lang_textview);
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
