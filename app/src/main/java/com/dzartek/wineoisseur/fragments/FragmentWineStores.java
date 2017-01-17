package com.dzartek.wineoisseur.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dzartek.wineoisseur.R;
import com.dzartek.wineoisseur.adapterviews.RvStoreResults;
import com.dzartek.wineoisseur.apicall.WineStoreApi;
import com.dzartek.wineoisseur.datamodel.CurrentLoc;
import com.dzartek.wineoisseur.datamodel.MyStore;
import com.dzartek.wineoisseur.pojolocation.LiquorStore;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dzarrillo on 11/4/2016.
 */

public class FragmentWineStores extends Fragment implements GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener{

    private final String TAG = FragmentWineStores.class.getName();
    private GoogleApiClient mGoogleApiClient;
    private final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private TextView txtOutput, textViewLastLocation;
    protected LocationRequest mLocationRequest;
    private Location mLastLocation;
    private String mOrigin;
    private String mDestin;
    private MyStore mMyStore;
    private ArrayList<MyStore> mStoreList = new ArrayList<>();
    private RecyclerView recyclerViewStores;
    private GridLayoutManager gridLayoutMgrStores;
    private RvStoreResults storeResultsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_wine_stores, container, false);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        if(!servicesAvailable()){
            showMyToast(getResources().getString(R.string.google_play_service_notavailable));
        }
        initializeRecyclerView(v);

        String screenName = getResources().getString(R.string.wine_store_locations);
        getActivity().setTitle(screenName);

        return v;
    }

    private void initializeRecyclerView(View v) {
        recyclerViewStores = (RecyclerView) v.findViewById(R.id.recyclerViewStores);
        gridLayoutMgrStores = new GridLayoutManager(getActivity(), 1);
        recyclerViewStores.setLayoutManager(gridLayoutMgrStores);
        recyclerViewStores.setHasFixedSize(true);
        storeResultsAdapter = new RvStoreResults(getActivity(), mStoreList);
        recyclerViewStores.setAdapter(storeResultsAdapter);

        recyclerViewStores.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerViewStores, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private final String TAG = RecyclerTouchListener.class.getName();
        private GestureDetector gestureDetector;
        private FragmentWineStores.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView,
                                     final FragmentWineStores.ClickListener clickListener){
           // Log.d(TAG, "constructor invoked!");
            this.clickListener = clickListener;

            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                   // Log.d(TAG, "onLongPress! " + e);
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
           // Log.d(TAG, "onTouchEvent!");
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10); // Update location every second

        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if(mLastLocation != null){

                    CurrentLoc.origin = mLastLocation.getLatitude() + "," + mLastLocation.getLongitude();
                }
                getApiData();
            }

        } else {

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if(mLastLocation != null) {
                CurrentLoc.origin = mLastLocation.getLatitude() + "," + mLastLocation.getLongitude();
                getApiData();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

      //  Log.i(TAG, "onRequestPermissionsResult!");

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION:
                showMyToast(getString(R.string.permission_granted));
        }

    }

    private boolean servicesAvailable() {
        GoogleApiAvailability googleApi = GoogleApiAvailability.getInstance();
        //GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        int resultCode = googleApi.isGooglePlayServicesAvailable(getActivity());

      //  Log.i(TAG, "Service available");
        if (ConnectionResult.SUCCESS == resultCode) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
      //  Log.i(TAG, "Connection suspended!");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
      //  Log.i(TAG, "Connection failed!" + connectionResult.getErrorCode());
    }

    @Override
    public void onLocationChanged(Location location) {
       // Log.i(TAG, "Connection suspended!");
        mGoogleApiClient.connect();
    }

    private void getApiData(){
        String radius = getResources().getString(R.string.radius);
        String liquorStore = getResources().getString(R.string.liquor_store);
        String googlePlacesKey = getResources().getString(R.string.google_places_key);

        WineStoreApi.Factory.getInstance().getWineStores(CurrentLoc.origin,
                radius, liquorStore, googlePlacesKey).enqueue(new Callback<LiquorStore>() {
            @Override
            public void onResponse(Call<LiquorStore> call, Response<LiquorStore> response) {
                if (response.isSuccessful()){
                    mStoreList.clear();
                    int num = response.body().getResults().size();

                    for(int i = 0; i < num; i++){
                        mMyStore = new MyStore();

                        mMyStore.setName(response.body().getResults().get(i).getName());
                        mMyStore.setVicinity(response.body().getResults().get(i).getVicinity());
                        mMyStore.setLatitude(response.body().getResults().get(i).getGeometry().getLocation().getLat());
                        mMyStore.setLongitude(response.body().getResults().get(i).getGeometry().getLocation().getLng());
                        mMyStore.setIcon(response.body().getResults().get(i).getIcon());
                        mMyStore.setPricelevel(response.body().getResults().get(i).getPriceLevel());


                        if (response.body().getResults().get(i).getOpeningHours() != null){
                            Boolean isOpen = response.body().getResults().get(i).getOpeningHours().isOpenNow();
                            if(isOpen){
                                mMyStore.setOpennow(getString(R.string.open_now));
                            } else{
                                mMyStore.setOpennow(getString(R.string.closed));
                            }
                        } else {
                            mMyStore.setOpennow(getString(R.string.closed));
                        }

                        mStoreList.add(mMyStore);
                    }

                    storeResultsAdapter.notifyDataSetChanged();
                } else{
                    ResponseBody errBody = response.errorBody();
                    showMyToast(errBody.toString());
                }
            }

            @Override
            public void onFailure(Call<LiquorStore> call, Throwable t) {
                showMyToast(getString(R.string.failed_to_get_data));
            }
        });
    }
}
