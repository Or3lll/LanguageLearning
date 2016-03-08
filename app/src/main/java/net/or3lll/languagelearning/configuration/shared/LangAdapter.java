package net.or3lll.languagelearning.configuration.shared;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.or3lll.languagelearning.R;
import net.or3lll.languagelearning.data.Lang;

import java.util.List;

/**
 * Created by or3lll on 22/02/2016.
 */
abstract public class LangAdapter extends BaseAdapter {

    protected List<Lang> mLangs;

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

    public int getPosition(long itemId) {
        for (int i = 0; i < mLangs.size(); i++) {
            Lang lang = mLangs.get(i);
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

        Lang lang = mLangs.get(position);
        if(lang != null) {
            Integer resIdFlag = Lang.flags.get(lang.isoCode);
            if(resIdFlag != null) {
                holder.getLangFlag().setImageResource(resIdFlag);
                holder.getLangFlag().setVisibility(View.VISIBLE);
            }
            else {
                holder.getLangFlag().setVisibility(View.INVISIBLE);
            }

            holder.getLangName().setText(lang.name);
        }

        return convertView;
    }

    private class Holder {
        private View mItemView;
        private ImageView mlangFlagImageView;
        private TextView mLangNameTextView;

        public Holder(View itemView) {
            mItemView = itemView;
        }

        public ImageView getLangFlag() {
            if(mlangFlagImageView == null) {
                mlangFlagImageView = (ImageView) mItemView.findViewById(R.id.langFlag);
            }

            return mlangFlagImageView;
        }

        public TextView getLangName() {
            if(mLangNameTextView == null) {
                mLangNameTextView = (TextView) mItemView.findViewById(R.id.langName);
            }

            return mLangNameTextView;
        }
    }
}
