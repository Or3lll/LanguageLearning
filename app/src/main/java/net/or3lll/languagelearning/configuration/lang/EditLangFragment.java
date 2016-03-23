package net.or3lll.languagelearning.configuration.lang;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import net.or3lll.languagelearning.R;
import net.or3lll.languagelearning.data.DataEventType;
import net.or3lll.languagelearning.data.Lang;

import java.util.List;


public class EditLangFragment extends Fragment {

    private static String LANG_ID_PARAM = "LANG_ID";


    private TableLangListener mListener;

    private EditText mNameEdit;
    private EditText mIsoCodeEdit;
    private Button mAddButton;

    private Lang mLang;


    public static EditLangFragment newInstance(long langId) {
        EditLangFragment fragment = new EditLangFragment();
        Bundle args = new Bundle();
        args.putLong(LANG_ID_PARAM, langId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_lang, container, false);

        mNameEdit = (EditText) v.findViewById(R.id.name_edit);
        mIsoCodeEdit = (EditText) v.findViewById(R.id.iso_code_edit);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                mAddButton.setEnabled(validation());
            }
        };
        mNameEdit.addTextChangedListener(textWatcher);
        mIsoCodeEdit.addTextChangedListener(textWatcher);

        mAddButton = (Button) v.findViewById(R.id.add_btn);
        mAddButton.setEnabled(false);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mLang != null) {
                    mLang.name = mNameEdit.getText().toString();
                    mLang.isoCode = mIsoCodeEdit.getText().toString();
                    mLang.save();
                    mListener.onTableLangEvent(DataEventType.UPDATE, mLang);
                }
                else {
                    Lang l = new Lang(mNameEdit.getText().toString(), mIsoCodeEdit.getText().toString());
                    l.save();
                    mListener.onTableLangEvent(DataEventType.CREATE, mLang);
                }
            }
        });

        mLang = Lang.findById(Lang.class, getArguments().getLong(LANG_ID_PARAM, -1));
        if(mLang != null) {
            mNameEdit.setText(mLang.name);
            mIsoCodeEdit.setText(mLang.isoCode);
            mAddButton.setText(R.string.button_update);

            mAddButton.setEnabled(validation());
        }

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TableLangListener) {
            mListener = (TableLangListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private boolean validation() {
        String name = mNameEdit.getText().toString();
        String isoCode = mIsoCodeEdit.getText().toString();

        if(name.length() > 0 && isoCode.length() >= 5) {
            List<Lang> langs = Lang.find(Lang.class, "name = ? or iso_Code = ?", name, isoCode);

            if(mLang != null) {
                return (langs.size() == 0) || (langs.size() == 1 && langs.get(0).getId() == mLang.getId());
            }
            else {
                return langs.size() == 0;
            }
        }

        return false;
    }
}
