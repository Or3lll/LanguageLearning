package net.or3lll.languagelearning.configuration.shared;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.orm.SugarRecord;

import net.or3lll.languagelearning.R;
import net.or3lll.languagelearning.data.Lang;

/**
 * Created by or3lll on 22/02/2016.
 */
abstract public class LangAdapter extends BaseAdapter {

    protected SparseArray<Lang> mValues;
    protected int mValuesNumber;

    @Override
    public int getCount() {
        return mValuesNumber;
    }

    @Override
    public Object getItem(int position) {
        Lang lang = mValues.get(position);
        if(lang == null) {
            lang = SugarRecord.find(Lang.class, null, null, null, "name", position + ", 1").get(0);
            mValues.append(position, lang);
        }
        return lang;
    }

    @Override
    public long getItemId(int position) {
        Lang lang = (Lang) getItem(position);
        if(lang != null) {
            return lang.getId();
        }

        return -1;
    }

    public int getPosition(long itemId) {
        for (int i = 0; i < mValuesNumber; i++) {
            Lang lang = (Lang) getItem(i);
            if(lang.getId() == itemId) {
                return i;
            }
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

        Lang lang = (Lang) getItem(position);
        if(lang != null) {
            holder.getLangFlag().setText(lang.getEmojiFlag());
            holder.getLangName().setText(lang.getName());
        }

        return convertView;
    }

    private class Holder {
        private View mItemView;
        private TextView mlangFlagTextView;
        private TextView mLangNameTextView;

        public Holder(View itemView) {
            mItemView = itemView;
        }

        public TextView getLangFlag() {
            if(mlangFlagTextView == null) {
                mlangFlagTextView = (TextView) mItemView.findViewById(R.id.langFlag);
            }

            return mlangFlagTextView;
        }

        public TextView getLangName() {
            if(mLangNameTextView == null) {
                mLangNameTextView = (TextView) mItemView.findViewById(R.id.langName);
            }

            return mLangNameTextView;
        }
    }
}
