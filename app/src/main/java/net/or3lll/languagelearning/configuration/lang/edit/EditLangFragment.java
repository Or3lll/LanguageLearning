package net.or3lll.languagelearning.configuration.lang.edit;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orm.SugarRecord;

import net.or3lll.languagelearning.BR;
import net.or3lll.languagelearning.R;
import net.or3lll.languagelearning.configuration.lang.list.TableLangListener;
import net.or3lll.languagelearning.data.DataEventType;
import net.or3lll.languagelearning.data.Lang;
import net.or3lll.languagelearning.databinding.FragmentLangEditBinding;
import net.or3lll.languagelearning.shared.SimpleTextWatcher;


public class EditLangFragment extends Fragment {

    private static String LANG_PARAM = "LANG";


    private TableLangListener mListener;

    private Lang mLang;

    private Observable.OnPropertyChangedCallback mPropertyChangedCallback;


    public static EditLangFragment newInstance(Lang lang) {
        EditLangFragment fragment = new EditLangFragment();
        Bundle args = new Bundle();
        args.putParcelable(LANG_PARAM, lang);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mLang = getArguments().getParcelable(LANG_PARAM);
        if(mLang == null) {
            mLang = new Lang();
        }

        FragmentLangEditBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_lang_edit, container, false);
        binding.setLang(mLang);

        binding.nameEdit.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                mLang.setName(s.toString());
            }
        });

        binding.flagEdit.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                mLang.setEmojiFlag(s.toString());
            }
        });

        binding.isoCodeEdit.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                mLang.setIsoCode(s.toString());
            }
        });

        binding.addBtn.setOnClickListener(v -> {
            DataEventType dataEventType = (mLang.getId() != null ? DataEventType.UPDATE : DataEventType.CREATE);
            SugarRecord.save(mLang);
            mListener.onTableLangEvent(dataEventType, mLang);
        });

        mPropertyChangedCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (propertyId == BR.validIsoCode) {
                    binding.isoCodeEditLayout.setError(mLang.isValidIsoCode() ? null :
                            getString(R.string.lang_edit_error_iso_code));
                }
            }
        };
        mLang.addOnPropertyChangedCallback(mPropertyChangedCallback);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mLang.removeOnPropertyChangedCallback(mPropertyChangedCallback);
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
}