package com.dzartek.wineoisseur.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.dzartek.wineoisseur.R;
import com.dzartek.wineoisseur.datamodel.SettingsTitle;
import com.dzartek.wineoisseur.utils.Constants;

import java.util.ArrayList;

/**
 * Created by dzarrillo on 12/13/2016.
 */

public class FragmentSettings extends Fragment implements View.OnClickListener{
    private final String TAG = FragmentSettings.class.getName();
    private ArrayList<SettingsTitle> mSettingsList = new ArrayList<>();
    private SharedPreferences mSettings;
    private String mSortby, mGetAllWines, mSearchResults;
    private RadioButton rdoBtnPriceAsc, rdoBtnPriceDesc, rdoBtnRanking,
            rdoBtnReturnFifty, rdoBtnReturnSeventyFive, rdoBtnReturn100,
            rdoBtnSearchSnooth, rdoBtnSearchAllWines;

    public FragmentSettings(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);

        // User settings
        mSettings = getActivity().getSharedPreferences(getResources().getString(R.string.preference_filename), 0);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        initializeWidgets(v);
        //Get user-Settings
        getUserSettings();

        String screenName = getResources().getString(R.string.action_settings);
        getActivity().setTitle(screenName);


        return v;
    }

    private void initializeWidgets(View v) {

        rdoBtnPriceDesc = (RadioButton) v.findViewById(R.id.radioButtonPriceDesc);
        rdoBtnPriceDesc.setOnClickListener(this);
        rdoBtnPriceAsc = (RadioButton) v.findViewById(R.id.radioButtonPriceAsc);
        rdoBtnPriceAsc.setOnClickListener(this);
        rdoBtnRanking = (RadioButton) v.findViewById(R.id.radioButtonRanking);
        rdoBtnRanking.setOnClickListener(this);
        rdoBtnReturnFifty = (RadioButton) v.findViewById(R.id.radioButtonReturnFifty);
        rdoBtnReturnFifty.setOnClickListener(this);
        rdoBtnReturnSeventyFive = (RadioButton) v.findViewById(R.id.radioButtonReturnSeventyFive);
        rdoBtnReturnSeventyFive.setOnClickListener(this);
        rdoBtnReturn100 = (RadioButton) v.findViewById(R.id.radioButtonReturn100);
        rdoBtnReturn100.setOnClickListener(this);
        rdoBtnSearchAllWines = (RadioButton) v.findViewById(R.id.radioButtonSearchAllWines);
        rdoBtnSearchAllWines.setOnClickListener(this);
        rdoBtnSearchSnooth = (RadioButton) v.findViewById(R.id.radioButtonSearchSnooth);
        rdoBtnSearchSnooth.setOnClickListener(this);
    }

    private void getUserSettings() {
        //mSettings.getString
//        ricerange = mSettings.getString(Constants.SETTINGS_PRICERANGE, Constants.SETTINGS_DEFAULT_PRICERANGE);
        mSortby = mSettings.getString(Constants.SETTINGS_SORTBY, getResources().getString(R.string.sortby_price_desc));

        switch (mSortby) {
            case "price+desc":
                rdoBtnPriceDesc.setChecked(true);
                break;
            case "price+asc":
                rdoBtnPriceAsc.setChecked(true);
                break;
            case "sr":
                rdoBtnRanking.setChecked(true);
                break;
         }

        mSearchResults = mSettings.getString(Constants.SETTINGS_RETURNRESULTS, getResources().getString(R.string.return_100_results));
        switch (mSearchResults) {
            case "50":
                rdoBtnReturnFifty.setChecked(true);
                break;
            case "75":
                rdoBtnReturnSeventyFive.setChecked(true);
                break;
            case "100":
                rdoBtnReturn100.setChecked(true);
                break;
        }

        mGetAllWines = mSettings.getString(Constants.SETTINGS_AVAILABLEWINES, getResources().getString(R.string.search_in_all));
        switch (mGetAllWines) {
            case "0":
                rdoBtnSearchAllWines.setChecked(true);
                break;
            case "1":
                rdoBtnSearchSnooth.setChecked(true);
                break;
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:

                if(((AppCompatActivity) getActivity()).getSupportActionBar()!= null){
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                }
                getActivity().getSupportFragmentManager().popBackStack();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            //  Sort by
            case R.id.radioButtonPriceAsc:
                saveUserSettings(Constants.SETTINGS_SORTBY, getResources().getString(R.string.sortby_price_asc) );
                break;
            case R.id.radioButtonPriceDesc:
                saveUserSettings(Constants.SETTINGS_SORTBY, getResources().getString(R.string.sortby_price_desc) );
                break;
            case R.id.radioButtonRanking:
                saveUserSettings(Constants.SETTINGS_SORTBY, getResources().getString(R.string.sortby_snooth_rank) );
                break;
            //  Number of results returned
            case R.id.radioButtonReturnFifty:
                saveUserSettings(Constants.SETTINGS_RETURNRESULTS, getResources().getString(R.string.return_fifty_results) );
                break;
            case R.id.radioButtonReturnSeventyFive:
                saveUserSettings(Constants.SETTINGS_RETURNRESULTS, getResources().getString(R.string.return_seventyfive_results) );
                break;
            case R.id.radioButtonReturn100:
                saveUserSettings(Constants.SETTINGS_RETURNRESULTS, getResources().getString(R.string.return_100_results) );
                break;
            //  Search in snooth inventory or all wines
            case R.id.radioButtonSearchSnooth:
                saveUserSettings(Constants.SETTINGS_AVAILABLEWINES, getResources().getString(R.string.search_in_snooth) );
                break;
            case R.id.radioButtonSearchAllWines:
                saveUserSettings(Constants.SETTINGS_AVAILABLEWINES, getResources().getString(R.string.search_in_all) );
                break;
        }
    }

    private void saveUserSettings(String key, String value){
        SharedPreferences sharedpreferences;

        sharedpreferences = this.getActivity().getSharedPreferences(getResources().getString(R.string.preference_filename), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = sharedpreferences.edit();

        editor.putString(key, value);

        editor.apply();
    }
}
