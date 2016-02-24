package net.or3lll.languagelearning.configuration.word;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.or3lll.languagelearning.R;
import net.or3lll.languagelearning.data.Lang;

import java.util.List;

/**
 * Created by or3lll on 22/02/2016.
 */
public class LangAdapter extends BaseAdapter {

    private List<Lang> mLangs;

    public LangAdapter() {
        mLangs = Lang.listAll(Lang.class);
    }

    @Override
    public int getCount() {
        return mLangs.size();
    }

    @Override
    public Object getItem(int position) {
        return mLangs.get(position);
    }

    @Override
    public long getItemId(int position) {
        Lang lang = mLangs.get(position);
        if(lang != null) {
            return lang.getId();
        }

        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;

        if(convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lang, null);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        }
        else {
            holder = (Holder) convertView.getTag();
        }

        Lang lang = mLangs.get(position);
        if(lang != null) {
            holder.getLangName().setText(lang.name);
        }

        return convertView;
    }

    private class Holder {
        private View mItemView;
        private TextView mLangNameTextView;

        public Holder(View itemView) {
            mItemView = itemView;
        }

        public TextView getLangName() {
            if(mLangNameTextView == null) {
                mLangNameTextView = (TextView) mItemView.findViewById(R.id.langName);
            }

            return mLangNameTextView;
        }
    }
}
