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
import net.or3lll.languagelearning.data.Lang;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditLangFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditLangFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditLangFragment extends Fragment {

    private static String LANG_ID_PARAM = "LANG_ID";


    private OnFragmentInteractionListener mListener;

    private EditText mNameEdit;
    private EditText mIsoCodeEdit;
    private Button mAddButton;

    private Lang mLang;


    public EditLangFragment() {
    }

    public void setListener(OnFragmentInteractionListener listener) {
        mListener = listener;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EditLangFragment.
     */
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
                    mListener.onLanguageUpdated();
                }
                else {
                    Lang l = new Lang(mNameEdit.getText().toString(), mIsoCodeEdit.getText().toString());
                    l.save();
                    mListener.onLanguageAdded();
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onLanguageAdded();
        void onLanguageUpdated();
    }
}
