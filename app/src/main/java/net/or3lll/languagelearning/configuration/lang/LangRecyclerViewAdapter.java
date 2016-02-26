package net.or3lll.languagelearning.configuration.lang;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.or3lll.languagelearning.R;
import net.or3lll.languagelearning.data.Lang;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Lang}
 */
public class LangRecyclerViewAdapter extends RecyclerView.Adapter<LangRecyclerViewAdapter.ViewHolder> {

    private List<Lang> mValues;
    private OnClickListener mListener;

    public LangRecyclerViewAdapter(List<Lang> items, OnClickListener listener) {
        mValues = items;
        mListener = listener;
    }

    public void setLangs(List<Lang> items) {
        mValues = items;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Lang lang = mValues.get(position);
        holder.mItem = lang;

        Integer resIdFlag = Lang.flags.get(lang.isoCode);
        if(resIdFlag != null) {
            holder.mLangFlag.setImageResource(resIdFlag);
            holder.mLangFlag.setVisibility(View.VISIBLE);
        }
        else {
            holder.mLangFlag.setVisibility(View.INVISIBLE);
        }
        holder.mlangName.setText(lang.name);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClick(holder.mItem);
                }
            }
        });

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
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mLangFlag;
        public final TextView mlangName;
        public Lang mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mLangFlag = (ImageView) view.findViewById(R.id.langFlag);
            mlangName = (TextView) view.findViewById(R.id.langName);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mlangName.getText() + "'";
        }
    }


    public interface OnClickListener {
        void onClick(Lang item);
        void onLongClick(Lang item);
    }
}
