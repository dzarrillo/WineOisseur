package com.dzartek.wineoisseur.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dzartek.wineoisseur.R;

/**
 * Created by dzarrillo on 1/10/2017.
 */

public class FragmentAboutVarietals extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragmentaboutvarietals, container, false);

        return v;
    }
}
