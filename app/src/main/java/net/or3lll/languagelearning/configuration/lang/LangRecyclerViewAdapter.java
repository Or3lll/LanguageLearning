package net.or3lll.languagelearning.configuration.lang;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.orm.SugarRecord;

import net.or3lll.languagelearning.R;
import net.or3lll.languagelearning.data.Lang;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Lang}
 */
public class LangRecyclerViewAdapter extends RecyclerView.Adapter<LangRecyclerViewAdapter.ViewHolder> {

    private SparseArray<Lang> mValues;
    private int mValuesNumber;
    private OnClickListener mListener;

    public LangRecyclerViewAdapter(OnClickListener listener) {
        mListener = listener;
        setLangs();
    }

    private void setLangs() {
        mValuesNumber = (int) SugarRecord.count(Lang.class);
        mValues = new SparseArray<>(mValuesNumber);
    }

    public void updateLangs() {
        setLangs();
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
        if(lang == null) {
            lang = SugarRecord.find(Lang.class, null, null, null, "name", position + ", 1").get(0);
            mValues.append(position, lang);
        }
        holder.mItem = lang;

        Integer resIdFlag = Lang.flags.get(lang.getIsoCode());
        if(resIdFlag != null) {
            holder.mLangFlag.setImageResource(resIdFlag);
            holder.mLangFlag.setVisibility(View.VISIBLE);
        }
        else {
            holder.mLangFlag.setVisibility(View.INVISIBLE);
        }
        holder.mlangName.setText(lang.getName());

        holder.mView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onClick(holder.mItem);
            }
        });

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
