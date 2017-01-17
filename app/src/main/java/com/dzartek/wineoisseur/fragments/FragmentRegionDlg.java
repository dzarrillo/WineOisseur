package com.dzartek.wineoisseur.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.dzartek.wineoisseur.R;

/**
 * Created by dzarrillo on 11/23/2016.
 */

public class FragmentRegionDlg extends DialogFragment implements View.OnClickListener{
    private final String TAG = FragmentRegionDlg.class.getName();


    private RadioButton rdoArgentina, rdoAustralia, rdoAustria, rdoBelgium, rdoCanada, rdoGermany,
        rdoNewZealand, rdoPortugal, rdoSpain;
    private Button btnOk, btnCancel;
    private String mRegion;

    public interface OnRegionSelectedListener{
        void onRegionSelected(String region);
    }
    private OnRegionSelectedListener mRegionCallback;

    public FragmentRegionDlg(){}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mRegion = savedInstanceState.getString(getString(R.string.region));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_regiondlg, container, false);

        getDialog().setTitle(R.string.region);

        initializeWidgets(v);
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(getString(R.string.region), mRegion);
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            mRegionCallback = (OnRegionSelectedListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() +
            getString(R.string.implement_OnRegionSelectedListener));
        }
    }

    private void initializeWidgets(View v) {
        rdoArgentina = (RadioButton) v.findViewById(R.id.radioButtonArgentina);
        rdoArgentina.setOnClickListener(this);
        rdoAustralia = (RadioButton) v.findViewById(R.id.radioButtonAustralia);
        rdoAustralia.setOnClickListener(this);
        rdoAustria = (RadioButton) v.findViewById(R.id.radioButtonAustria);
        rdoAustria.setOnClickListener(this);
        rdoBelgium = (RadioButton) v.findViewById(R.id.radioButtonBelgium);
        rdoBelgium.setOnClickListener(this);
        rdoCanada = (RadioButton) v.findViewById(R.id.radioButtonCanada);
        rdoCanada.setOnClickListener(this);
        rdoGermany = (RadioButton) v.findViewById(R.id.radioButtonGermany);
        rdoGermany.setOnClickListener(this);
        rdoNewZealand = (RadioButton) v.findViewById(R.id.radioButtonNewZealand);
        rdoNewZealand.setOnClickListener(this);
        rdoPortugal = (RadioButton) v.findViewById(R.id.radioButtonPortugal);
        rdoPortugal.setOnClickListener(this);
        rdoSpain = (RadioButton) v.findViewById(R.id.radioButtonSpain);
        rdoSpain.setOnClickListener(this);

        btnCancel = (Button) v.findViewById(R.id.buttonCancel);
        btnCancel.setOnClickListener(this);
        btnOk = (Button) v.findViewById(R.id.buttonOk);
        btnOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.radioButtonArgentina:
                mRegion = getString(R.string.argentina);
                break;
            case R.id.radioButtonAustria:
                mRegion = getString(R.string.austria);
                break;
            case R.id.radioButtonAustralia:
                mRegion = getString(R.string.australia);
                break;
            case R.id.radioButtonBelgium:
                mRegion = getString(R.string.belgium);
                break;
            case R.id.radioButtonCanada:
                mRegion = getString(R.string.canada);
                break;
            case R.id.radioButtonGermany:
                mRegion = getString(R.string.germany);
                break;
            case R.id.radioButtonNewZealand:
                mRegion = getString(R.string.new_zealand);
                break;
            case R.id.radioButtonPortugal:
                mRegion = getString(R.string.portugal);
                break;
            case R.id.radioButtonSpain:
                mRegion = getString(R.string.spain);
                break;
            case R.id.buttonOk:
                if (mRegion == null){
                    Toast.makeText(getActivity(), getString(R.string.please_make_selection), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    mRegionCallback.onRegionSelected(mRegion);
                }
                dismiss();
                break;
            case R.id.buttonCancel:
                mRegion="";
                dismiss();
                break;
        }
    }
}
