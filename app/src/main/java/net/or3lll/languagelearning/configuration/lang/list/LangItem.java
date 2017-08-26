package net.or3lll.languagelearning.configuration.lang.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.fastadapter.items.GenericAbstractItem;

import net.or3lll.languagelearning.R;
import net.or3lll.languagelearning.data.Lang;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by or3lll on 27/08/2017.
 */

public class LangItem extends GenericAbstractItem<Lang, LangItem, LangItem.ViewHolder> {

    public LangItem(Lang lang) {
        super(lang);
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.item_lang;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_lang;
    }

    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);

        Lang lang = getModel();

        holder.mLangFlag.setText(lang.getEmojiFlag());
        holder.mlangName.setText(lang.getName());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.langFlag) TextView mLangFlag;
        @BindView(R.id.langName) TextView mlangName;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mlangName.getText() + "'";
        }
    }
}
