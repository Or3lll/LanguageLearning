package net.or3lll.languagelearning.configuration.word.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.fastadapter.items.GenericAbstractItem;

import net.or3lll.languagelearning.R;
import net.or3lll.languagelearning.data.Word;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by or3lll on 27/08/2017.
 */

public class WordItem extends GenericAbstractItem<Word, WordItem, WordItem.ViewHolder> {

    public WordItem(Word word) {
        super(word);
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.item_word_simple;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_word_simple;
    }

    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);

        Word word = getModel();
        holder.mContentView.setText(word.text);

        /*holder.mContentView.setOnClickListener(v -> {
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
        });*/
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.content) TextView mContentView;
        @BindView(R.id.edit) ImageView mEdit;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
