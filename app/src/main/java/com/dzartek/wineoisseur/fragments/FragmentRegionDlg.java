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
    private final String ARGENTINA = "ar";
    private final String AUSTRALIA = "au";
    private final String AUSTRIA = "at";
    private final String BELGIUM = "be";
    private final String CANADA = "ca";
    private final String GERMANY = "de";
    private final String NEWZEALNAD = "nz";
    private final String PORTUGAL = "pt";
    private final String SPAIN = "es";

    private RadioButton rdoArgentina, rdoAustralia, rdoAustria, rdoBelgium, rdoCanada, rdoGermany,
        rdoNewZealand, rdoPortugal, rdoSpain;
    private Button btnOk, btnCancel;
    private String mRegion;

    public interface OnRegionSelectedListener{
        void onRegionSelected(String region);
    }
    private OnRegionSelectedListener mRegionCallback;

    public FragmentRegionDlg(){}


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_regiondlg, container, false);

        getDialog().setTitle("Regions");

        initializeWidgets(v);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            mRegionCallback = (OnRegionSelectedListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() +
            " must implement OnRegionSelectedListener!");
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
                mRegion = ARGENTINA;
                break;
            case R.id.radioButtonAustria:
                mRegion = AUSTRIA;
                break;
            case R.id.radioButtonAustralia:
                mRegion = AUSTRALIA;
                break;
            case R.id.radioButtonBelgium:
                mRegion = BELGIUM;
                break;
            case R.id.radioButtonCanada:
                mRegion = CANADA;
                break;
            case R.id.radioButtonGermany:
                mRegion = GERMANY;
                break;
            case R.id.radioButtonNewZealand:
                mRegion = NEWZEALNAD;
                break;
            case R.id.radioButtonPortugal:
                mRegion = PORTUGAL;
                break;
            case R.id.radioButtonSpain:
                mRegion = SPAIN;
                break;
            case R.id.buttonOk:
                if (mRegion == null){
                    Toast.makeText(getActivity(), "Please make selection!", Toast.LENGTH_SHORT).show();
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
