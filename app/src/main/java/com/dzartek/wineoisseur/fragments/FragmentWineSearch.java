package com.dzartek.wineoisseur.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dzartek.wineoisseur.BuildConfig;
import com.dzartek.wineoisseur.R;
import com.dzartek.wineoisseur.adapterviews.RvVarietalAdapter;
import com.dzartek.wineoisseur.apicall.WineApi;
import com.dzartek.wineoisseur.broadcastreceiver.FavoriteWineReceiver;
import com.dzartek.wineoisseur.datamodel.MyWine;
import com.dzartek.wineoisseur.pojomodel.SnoothWine;
import com.dzartek.wineoisseur.pojomodel.Wine;
import com.dzartek.wineoisseur.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by dzarrillo on 10/4/2016.
 */

public class FragmentWineSearch extends Fragment implements OnClickListener {

    private final String TAG = "FragmentWineSearch";
    // RadioGroups
    private RadioGroup radioGroupPriceRangeOne, radioGroupPriceRangeTwo;
    // RadioButtons
    private RadioButton radioButtonZero, radioButtonTwenty, radioButtonForty, radioButtonTen,
            radioButtonThirty, radioButtonFifty, radioButtonAll, radioButtonFive, radioButtonFour,
            radioButtonTwo, radioButtonThree, radioButtonRed, radioButtonWhite, radioButtonUsa,
            radioButtonFrance, radioButtonItaly, radioButtonShowMore;

    // To hold the different wine varieties - Zinfandel, merlot, malbec ...
    private ArrayList<String> mVarietalList = new ArrayList<>();
    //RecyclerView
    private RecyclerView mRvVarietal;
    private RvVarietalAdapter mRvVarietalAdapter;
    private GridLayoutManager mGridLayoutManager;
    private Resources mResources;
    private Boolean mBooleanRed = true;
    // Button
    private Button buttonSearch;
    private ProgressDialog mProgress;

    // search criteria
    private String mRegion, mMinRank, mMaxRank, mMinPrice, mMaxPrice, mVarietal,
            mWineColor, mSortby, mGetAllWines, mSearchResults;
    // Snooth.com API key
    private String WINE_API_KEY;
    // User settings
    SharedPreferences mSettings;
    private FavoriteWineReceiver mFavoriteWineReceiver;

    private ArrayList<MyWine> myWineList = new ArrayList<>();


    // interface to send results found to the results page
    public interface OnWineResultsFound {
        void onWineFound(ArrayList<MyWine> wineList, ProgressDialog mProgress);
    }
    private OnWineResultsFound mWineFoundCallback;

    public FragmentWineSearch() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // To get access to the strings.xml values
        mResources = getResources();
        WINE_API_KEY = mResources.getString(R.string.wine_api_key);
        // User settings
        mSettings = getActivity().getSharedPreferences(getResources().getString(R.string.preference_filename), 0);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_wine_search, container, false);

        initializeRadioButtons(v);
        initializeRecyclerView(v);
        initializeButton(v);

        //Get user-Settings
        getUserSettings();

        if (savedInstanceState == null) {
            // Request focus at top of screen
            v.findViewById(R.id.recyclerViewVarietal).setFocusable(false);
            v.findViewById(R.id.TextViewRegion).requestFocus();

        }

        String screenName = getResources().getString(R.string.search_for_wines);
        getActivity().setTitle(screenName);
        return v;

    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter(FavoriteWineReceiver.PROCESS_RESPONSE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        mFavoriteWineReceiver = new FavoriteWineReceiver();
        getActivity().registerReceiver(mFavoriteWineReceiver, filter);

    }

    private void getUserSettings() {

        mSortby = mSettings.getString(Constants.SETTINGS_SORTBY, getResources().getString(R.string.sortby_price_desc));

        mSearchResults = mSettings.getString(Constants.SETTINGS_RETURNRESULTS, getResources().getString(R.string.return_100_results));

        mGetAllWines = mSettings.getString(Constants.SETTINGS_AVAILABLEWINES, getResources().getString(R.string.search_in_all));


        // Price range
        String pricerange = mSettings.getString(Constants.SETTINGS_PRICERANGE, Constants.SETTINGS_DEFAULT_PRICERANGE);
        switch (pricerange) {
            case "0":
                radioButtonZero.setChecked(true);
                mMinPrice = "0";
                mMaxPrice = "10";
                break;
            case "10":
                radioButtonTen.setChecked(true);
                mMinPrice = "10";
                mMaxPrice = "20";
                break;
            case "20":
                radioButtonTwenty.setChecked(true);
                mMinPrice = "20";
                mMaxPrice = "30";
                break;
            case "30":
                radioButtonThirty.setChecked(true);
                mMinPrice = "30";
                mMaxPrice = "40";
                break;
            case "40":
                radioButtonForty.setChecked(true);
                mMinPrice = "40";
                mMaxPrice = "50";
                break;
            case "50":
                radioButtonFifty.setChecked(true);
                mMinPrice = "50";
                mMaxPrice = "2000";
                break;
        }

        // Red or white - Constants.
        mWineColor = mSettings.getString(Constants.SETTINGS_WINECOLOR, Constants.SETTINGS_DEFAULT_WINECOLOR);
        switch (mWineColor) {
            case "WHITE":
                //radioButtonWhite.setChecked(true);
                populateVarietal(false);
                break;
            case "RED":
                //radioButtonRed.setChecked(true);
                populateVarietal(true);
                break;
        }

        // Rating
        String maxrating = mSettings.getString(Constants.SETTINGS_MAX_RATING, Constants.SETTINGS_DEFAULT_RATING);

        switch (maxrating) {
            case "2":
                radioButtonTwo.setChecked(true);
                mMinRank = "2";
                mMaxRank = "2";
                break;
            case "3":
                radioButtonThree.setChecked(true);
                mMinRank = "3";
                mMaxRank = "3";
                break;
            case "4":
                radioButtonFour.setChecked(true);
                mMinRank = "4";
                mMaxRank = "4";
                break;
            case "5":
                radioButtonFive.setChecked(true);
                mMinRank = "5";
                mMaxRank = "5";
                break;
        }

        // Region
        mRegion = mSettings.getString(Constants.SETTINGS_REGION, Constants.SETTINGS_DEFAULT_REGION);
        switch (mRegion) {
            case Constants.USA:
                radioButtonUsa.setChecked(true);
                break;
            case Constants.FRANCE:
                radioButtonFrance.setChecked(true);
                break;
            case Constants.ITALY:
                radioButtonItaly.setChecked(true);
                break;
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        // Make sure host implements method
        try {
            mWineFoundCallback = (OnWineResultsFound) getActivity();
        } catch (Exception e) {
            throw new ClassCastException(getActivity().toString()
                    + " Must implement OnWineResultsFound");
        }
    }

    @Override
    public void onDestroy() {
//        getActivity().unregisterReceiver(mFavoriteWineReceiver);
        super.onDestroy();

    }

    @Override
    public void onStop() {
        getActivity().unregisterReceiver(mFavoriteWineReceiver);
        super.onStop();
    }

    private void initializeButton(View v) {
        buttonSearch = (Button) v.findViewById(R.id.buttonSearch);

        buttonSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                mProgress = ProgressDialog.show(getActivity(), "Searching...", "Please wait...", true);
                // Save search criteria to usersettings
                saveUserSettings();
                
                // DZ - consume web service
                getApiData();

            }
        });
    }

    private void saveUserSettings() {


        if (mRegion != null) {
            SharedPreferences sharedpreferences;

            sharedpreferences = this.getActivity().getSharedPreferences(getResources().getString(R.string.preference_filename), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor;
            editor = sharedpreferences.edit();

            // Region
            editor.putString(Constants.SETTINGS_REGION, mRegion);
            // Rating
            editor.putString(Constants.SETTINGS_MIN_RATING, mMinRank);
            editor.putString(Constants.SETTINGS_MAX_RATING, mMaxRank);
            // Color
            editor.putString(Constants.SETTINGS_WINECOLOR, mWineColor);
            // price
            editor.putString(Constants.SETTINGS_PRICERANGE, mMinPrice);
            // Varietal
            editor.putString(Constants.SETTINGS_VARIETAL, mVarietal);
            editor.apply();
        }

    }


    private void getApiData() {

        if (mRegion != null) {
            WineApi.Factory.getInstance().getWines(WINE_API_KEY, mVarietal, mSearchResults,
                    mGetAllWines, mRegion, mSortby, mMinPrice, mMaxPrice, mMinRank, mMaxRank)
                    .enqueue(new Callback<SnoothWine>() {

                        @Override
                        public void onResponse(Call<SnoothWine> call, Response<SnoothWine> response) {
                            if (response.isSuccessful()) {
                                myWineList.clear();
                                int id = 1;
                                //  pojomodel/Wine
                                for (Wine wine : response.body().getWines()) {
                                    MyWine mywine = new MyWine();
                                    mywine.setId(id);
                                    mywine.setRegion(wine.getRegion());
                                    mywine.setVarietal(wine.getVarietal());
                                    mywine.setName(wine.getName());
                                    mywine.setCode(wine.getCode());
                                    mywine.setWinery(wine.getWinery());
                                    mywine.setPrice(wine.getPrice());
                                    mywine.setVintage(wine.getVintage());
                                    mywine.setLink(wine.getLink());
                                    mywine.setImage(wine.getImage());
                                    mywine.setSnoothrank(wine.getSnoothrank());
                                    myWineList.add(mywine);
                                }

                                //  Send data to the MainWineActivity
                                if (myWineList.size() > 0) {
//                                    mProgress.dismiss();
                                    mWineFoundCallback.onWineFound(myWineList, mProgress);
                                } else {
                                    mProgress.dismiss();
                                    showMyToast("No Results Found!");
                                }

                            } else {
                                mProgress.dismiss();
                                ResponseBody errBody = response.errorBody();
                              //  Log.d(TAG, errBody.toString());
                                showMyToast(errBody.toString());
                            }
                        }

                        @Override
                        public void onFailure(Call<SnoothWine> call, Throwable t) {
                            mProgress.dismiss();
                            showMyToast("Failed to get Data!");
                        }
                    });
        } else {
            mProgress.dismiss();
            showMyToast("Missing region!");
        }
    }


    private void initializeRecyclerView(View v) {

        populateVarietal(mBooleanRed);
        mRvVarietal = (RecyclerView) v.findViewById(R.id.recyclerViewVarietal);

        mGridLayoutManager = new GridLayoutManager(getActivity(), 1);
        mRvVarietal.setLayoutManager(mGridLayoutManager);
        mRvVarietal.setHasFixedSize(true);

        //  Get user setting for varietal and pass it to the adapterview - Constants.
        mVarietal = mSettings.getString(Constants.SETTINGS_VARIETAL, Constants.SETTINGS_DEFAULT_VARIETAL);
        mRvVarietalAdapter = new RvVarietalAdapter(mVarietalList, mVarietal);
        mRvVarietal.setAdapter(mRvVarietalAdapter);

        mRvVarietal.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), mRvVarietal, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
                mVarietal = mVarietalList.get(position);
            }
        }));

    }

    private void populateVarietal(boolean blnRed) {
        String[] variety;
        mVarietalList.clear();
        if (blnRed) {
            radioButtonRed.setChecked(true);
            variety = mResources.getStringArray(R.array.red_varietal);
        } else {
            radioButtonWhite.setChecked(true);
            variety = mResources.getStringArray(R.array.white_varietal);
        }
        Collections.addAll(mVarietalList, variety);

    }

    private void initializeRadioButtons(View v) {

        //RadioGrouppriceRangeOne
        radioGroupPriceRangeOne = (RadioGroup) v.findViewById(R.id.radioGroupPriceRangeOne);
        radioGroupPriceRangeOne.setOnClickListener(this);
        radioButtonZero = (RadioButton) v.findViewById(R.id.radioButtonZero);
        radioButtonZero.setOnClickListener(this);
        radioButtonTwenty = (RadioButton) v.findViewById(R.id.radioButtonTwenty);
        radioButtonTwenty.setOnClickListener(this);
        radioButtonForty = (RadioButton) v.findViewById(R.id.radioButtonForty);
        radioButtonForty.setOnClickListener(this);
        //RadioGrouppriceRangeTwo
        radioGroupPriceRangeTwo = (RadioGroup) v.findViewById(R.id.radioGroupPriceRangeTwo);
        radioGroupPriceRangeTwo.setOnClickListener(this);
        radioButtonTen = (RadioButton) v.findViewById(R.id.radioButtonTen);
        radioButtonTen.setOnClickListener(this);
        radioButtonThirty = (RadioButton) v.findViewById(R.id.radioButtonThirty);
        radioButtonThirty.setOnClickListener(this);
        radioButtonFifty = (RadioButton) v.findViewById(R.id.radioButtonFifty);
        radioButtonFifty.setOnClickListener(this);

        //RadioGrouppriceRating
        radioButtonFive = (RadioButton) v.findViewById(R.id.radioButtonFive);
        radioButtonFive.setOnClickListener(this);
        radioButtonFour = (RadioButton) v.findViewById(R.id.radioButtonFour);
        radioButtonFour.setOnClickListener(this);
        radioButtonThree = (RadioButton) v.findViewById(R.id.radioButtonThree);
        radioButtonThree.setOnClickListener(this);
        radioButtonTwo = (RadioButton) v.findViewById(R.id.radioButtonTwo);
        radioButtonTwo.setOnClickListener(this);

        //RadioGroupVarietal
        radioButtonRed = (RadioButton) v.findViewById(R.id.radioButtonRed);
        radioButtonRed.setOnClickListener(this);
        radioButtonWhite = (RadioButton) v.findViewById(R.id.radioButtonWhite);
        radioButtonWhite.setOnClickListener(this);

        //RadioGroupRegion
        radioButtonUsa = (RadioButton) v.findViewById(R.id.radioButtonUSA);
        radioButtonUsa.setOnClickListener(this);
        radioButtonFrance = (RadioButton) v.findViewById(R.id.radioButtonFrance);
        radioButtonFrance.setOnClickListener(this);
        radioButtonItaly = (RadioButton) v.findViewById(R.id.radioButtonItaly);
        radioButtonItaly.setOnClickListener(this);
        radioButtonShowMore = (RadioButton) v.findViewById(R.id.radioButtonShowMore);
        radioButtonShowMore.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //  Radiogroup price range
            case R.id.radioButtonZero:
                radioGroupPriceRangeTwo.clearCheck();

                radioButtonZero.setChecked(true);
                mMinPrice = "0";
                mMaxPrice = "10";
                radioButtonTen.setChecked(false);
                radioButtonThirty.setChecked(false);
                radioButtonFifty.setChecked(false);

                break;
            case R.id.radioButtonTwenty:
                radioGroupPriceRangeTwo.clearCheck();
                radioButtonTwenty.setChecked(true);
                mMinPrice = "20";
                mMaxPrice = "30";
                radioButtonTen.setChecked(false);
                radioButtonThirty.setChecked(false);
                radioButtonFifty.setChecked(false);

                break;
            case R.id.radioButtonForty:
                radioGroupPriceRangeTwo.clearCheck();
                radioButtonForty.setChecked(true);
                mMinPrice = "40";
                mMaxPrice = "50";
                radioButtonTen.setChecked(false);
                radioButtonThirty.setChecked(false);
                radioButtonFifty.setChecked(false);

                break;
            case R.id.radioButtonTen:
                radioGroupPriceRangeOne.clearCheck();
                radioButtonTen.setChecked(true);
                radioButtonTen.isSelected();
                mMinPrice = "10";
                mMaxPrice = "20";
                radioButtonZero.setChecked(false);
                radioButtonTwenty.setChecked(false);
                radioButtonForty.setChecked(false);

                break;
            case R.id.radioButtonThirty:
                radioGroupPriceRangeOne.clearCheck();
                radioButtonThirty.setChecked(true);
                mMinPrice = "30";
                mMaxPrice = "40";
                radioButtonZero.setChecked(false);
                radioButtonTwenty.setChecked(false);
                radioButtonForty.setChecked(false);

                break;
            case R.id.radioButtonFifty:
                radioGroupPriceRangeOne.clearCheck();
                radioButtonFifty.setChecked(true);
                mMinPrice = "50";
                mMaxPrice = "1000";
                radioButtonZero.setChecked(false);
                radioButtonTwenty.setChecked(false);
                radioButtonForty.setChecked(false);
                break;

            //  Radiogroup Rating
            case R.id.radioButtonFive:
                mMinRank = Constants.FIVE;
                mMaxRank = Constants.FIVE;
//                Toast.makeText(getActivity(), "5 stars selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.radioButtonFour:
                mMinRank = Constants.FOUR;
                mMaxRank = Constants.FOUR;
//                Toast.makeText(getActivity(), "4 stars selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.radioButtonThree:
                mMinRank = Constants.THREE;
                mMaxRank = Constants.THREE;
//                Toast.makeText(getActivity(), "3 stars selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.radioButtonTwo:
                mMinRank = Constants.TWO;
                mMaxRank = Constants.TWO;
//                Toast.makeText(getActivity(), "2 stars selected", Toast.LENGTH_SHORT).show();
                break;

            // Wine Color - red/white
            case R.id.radioButtonRed:
                mWineColor = Constants.RED;
                populateVarietal(true);
                mRvVarietalAdapter.notifyDataSetChanged();
//                mRvVarietalAdapter.notifyItemRangeChanged(0, mVarietals.length);
                break;
            case R.id.radioButtonWhite:
                mWineColor = Constants.WHITE;
                populateVarietal(false);
                mRvVarietalAdapter.notifyDataSetChanged();
//                mRvVarietalAdapter.notifyItemRangeChanged(0, mVarietals.length);
                break;
            //  RadioButtonRegion
            case R.id.radioButtonUSA:
                mRegion = Constants.USA;
                break;
            case R.id.radioButtonFrance:
                mRegion = Constants.FRANCE;
                break;
            case R.id.radioButtonItaly:
                mRegion = Constants.ITALY;
                break;
            case R.id.radioButtonShowMore:
                FragmentRegionDlg fragRegionDlg = new FragmentRegionDlg();
                fragRegionDlg.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

                fragRegionDlg.show(getFragmentManager(), "Region");
                break;
        }
    }


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private final String TAG = "RecyclerTouchListener";
        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
           // Log.d(TAG, "constructor invoked!");
            this.clickListener = clickListener;

            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                 //   Log.d(TAG, "onLongPress! " + e);
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onLongClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
          //  Log.d(TAG, "onTouchEvent!");
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    //  Used by the FragmentRegionDlg triggering event in MainWineActivity
    public void updateRegion(String region) {
        mRegion = region;
        showMyToast("Region " + mRegion);
    }

    public void showMyToast(String msg) {
        TextView tv = new TextView(getActivity());
        tv.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.cardview_light_background));
        tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        tv.setPadding(25, 25, 25, 25);
        tv.setTextSize(19);
        tv.setTypeface(null, Typeface.NORMAL);
        tv.setText(msg);

        Toast tt = new Toast(getActivity());
        tt.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 150);
        tt.setView(tv);
        tt.show();
    }
}
