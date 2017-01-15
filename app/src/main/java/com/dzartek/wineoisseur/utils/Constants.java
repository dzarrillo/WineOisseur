package com.dzartek.wineoisseur.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.dzartek.wineoisseur.BuildConfig;

/**
 * Created by dzarrillo on 11/15/2016.
 */

public final class Constants {
    // User setting keys
    public static final String SETTINGS_REGION = "REGION";
    public static final String SETTINGS_MIN_RATING = "MINRATING";
    public static final String SETTINGS_MAX_RATING = "MAXRATING";
    public static final String SETTINGS_WINECOLOR = "WINECOLOR";
    public static final String SETTINGS_PRICERANGE = "PRICERANGE";
    public static final String SETTINGS_VARIETAL = "VARIETAL";
    public static final String SETTINGS_SORTBY = "SORTBY";
    public static final String SETTINGS_RETURNRESULTS = "RETURNRESULTS";
    public static final String SETTINGS_AVAILABLEWINES = "AVAILABLEWINES";

    // User settings default values
    public static final String SETTINGS_DEFAULT_REGION = "us";
    public static final String SETTINGS_DEFAULT_RATING = "3";
    public static final String SETTINGS_DEFAULT_WINECOLOR = "RED";
    public static final String SETTINGS_DEFAULT_PRICERANGE = "0";
    public static final String SETTINGS_DEFAULT_VARIETAL = "Merlot";
    // Regions
    public static final String USA = "us";
    public static final String FRANCE = "fr";
    public static final String ITALY = "it";
    // Varietals
    public static final String RED = "RED";
    public static final String WHITE = "WHITE";
    // Ranking
    public static final String THREE = "3";
    public static final String FOUR = "4";
    public static final String FIVE = "5";
    public static final String TWO = "2";
    // FavoriteWinesIntentService
    public static final String ACTION_ISFAVORITE = "ISFAVORITE";
    public static final String ACTION_DELETE = "DELETE";
    public static final String ACTION_SAVE = "SAVE";
    public static final String RESPONSE_STRING = "RESPONSESTRING";
    public static final String RESPONSE_MESSAGE = "RESPONSEMESSAGE";

    // Dessert serch criteria
    public static final String DESSERT_NAME = "dessert";
    public static final String DESSERT_MIN_RANK = "3";
    public static final String DESSERT_MAX_RANK = "5";
    public static final String DESSERT_MIN_PRICE = "7";
    public static final String DESSERT_MAX_PRICE = "100";
    public static final String DESSERT_SORTBY = "sr";
    public static final String DESSERT_NUM_RESULTS = "50";

    // image size for picasso
    public static final int TABLETIMAGE = 200;
    public static final int CELLIMAGE = 400;
    // If tablet then use TABLETIMAGE else use CELLIMAGE
    public static boolean isTablet = false;
    public static final int tabletImageSize = 150;
    public static final int phoneImageSize = 400;

    public Constants() {
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
