package net.or3lll.languagelearning.configuration.lang;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.common.base.CharMatcher;

import net.or3lll.languagelearning.R;
import net.or3lll.languagelearning.data.Lang;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddLangFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddLangFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddLangFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private EditText nameEdit;
    private EditText isoCodeEdit;
    private Button addButton;


    public AddLangFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddLangFragment.
     */
    public static AddLangFragment newInstance() {
        AddLangFragment fragment = new AddLangFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_lang, container, false);

        nameEdit = (EditText) v.findViewById(R.id.name_edit);
        isoCodeEdit = (EditText) v.findViewById(R.id.iso_code_edit);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                addButton.setEnabled(validation());
            }
        };
        nameEdit.addTextChangedListener(textWatcher);
        isoCodeEdit.addTextChangedListener(textWatcher);

        addButton = (Button) v.findViewById(R.id.add_btn);
        addButton.setEnabled(false);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Lang l = new Lang(nameEdit.getText().toString(), isoCodeEdit.getText().toString());
                l.save();
            }
        });

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
        String name = nameEdit.getText().toString();
        String isoCode = isoCodeEdit.getText().toString();


        Lang.find(Lang.class, "name = ? or isoCode = ?", name, "fr_FR");



        int l1 = name.length();
        int l2 = isoCode.length();

        //if(l1 != 0)
        //    if(l2 == l2) {

                //return Lang.count (Lang.class, "name = ?", name) == 0;
        //    }

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
        void onFragmentInteraction();
    }
}
