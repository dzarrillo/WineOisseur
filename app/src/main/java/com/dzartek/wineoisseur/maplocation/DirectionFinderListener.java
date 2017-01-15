package com.dzartek.wineoisseur.maplocation;

import java.util.List;

/**
 * Created by dzarrillo on 11/3/2016.
 */

public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
