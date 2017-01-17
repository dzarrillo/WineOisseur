package com.dzartek.wineoisseur;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.dzartek.wineoisseur.apicall.WineApi;
import com.dzartek.wineoisseur.datamodel.MyWine;
import com.dzartek.wineoisseur.fragments.FragmentAboutVarietals;
import com.dzartek.wineoisseur.fragments.FragmentFavoriteWines;
import com.dzartek.wineoisseur.fragments.FragmentRegionDlg;
import com.dzartek.wineoisseur.fragments.FragmentSettings;
import com.dzartek.wineoisseur.fragments.FragmentWineResults;
import com.dzartek.wineoisseur.fragments.FragmentWineSearch;
import com.dzartek.wineoisseur.fragments.FragmentWineStores;
import com.dzartek.wineoisseur.pojodessert.DessertWine;
import com.dzartek.wineoisseur.pojodessert.Wine;
import com.dzartek.wineoisseur.utils.Constants;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainWineActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        FragmentWineSearch.OnWineResultsFound, FragmentRegionDlg.OnRegionSelectedListener {
    private final String TAG = MainWineActivity.class.getName();
    private ArrayList<MyWine> myWineList = new ArrayList<>();
    private ActionBarDrawerToggle toggle;
    // Identifies a particular Loader being used in this component
    private static final int URL_LOADER = 0;
    private FirebaseAnalytics mFirebaseAnalytics;

    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_wine);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Obtain the Firebase Analytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);
        mFirebaseAnalytics.setMinimumSessionDuration(20000);
        mFirebaseAnalytics.setSessionTimeoutDuration(500);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);

        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            // If not online get favorites
            if (!Constants.isOnline(this)) {
                Toast.makeText(this, getString(R.string.network_is_not_available)
                                + "\n" + getString(R.string.fetching_favorites),
                        Toast.LENGTH_SHORT).show();
                // Get data from favoritewines database
                getFavorites();
            } else {
                Fragment newFragment;
                // Check to see if smartphone (one panel) or tablet (two panel)
                if (findViewById(R.id.tablet) == null) {
                    Constants.isTablet = false;
                    newFragment = new FragmentWineSearch();

                } else {
                    Constants.isTablet = true;
                    newFragment = new FragmentAboutVarietals();   //FragmentFavoriteWines();
                }

                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.slide_from_left, R.anim.slide_from_right,
                                R.anim.slide_from_left, R.anim.slide_from_right)
                        .add(R.id.fragmentHolder, newFragment)
                        .commit();
            }
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_in_bottom);
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_wine, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Resources res = getResources();
        this.setTitle(res.getString(R.string.app_name));

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        toggle.syncState();
        if (id == R.id.nav_wineSearch) {
            if (!Constants.isOnline(this)) {
                Toast.makeText(this, getString(R.string.network_is_not_available)
                                + "\n" + getString(R.string.fetching_favorites),
                         Toast.LENGTH_SHORT).show();
                getFavorites();
            } else {

                // Check to see if smartphone (one panel) or tablet (two panel)
                if (findViewById(R.id.tablet) == null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.slide_from_left, R.anim.slide_in_bottom,
                                    R.anim.slide_from_left, R.anim.slide_in_bottom)
                            .addToBackStack(null)
                            .replace(R.id.fragmentHolder, new FragmentWineSearch())
                            .commit();
                } else {
                    // Tablet

                }
            }

        } else if (id == R.id.nav_dessert) {
            if (!Constants.isOnline(this)) {
                // Do nothing
            } else {
                mProgress = ProgressDialog.show(this, getString(R.string.searching), getString(R.string.please_wait), true);
                getApiData();
            }

        } else if (id == R.id.nav_favorites) {
            getFavorites();

        } else if (id == R.id.nav_winestores) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_from_left, R.anim.slide_from_right,
                            R.anim.slide_from_left, R.anim.slide_from_right) //R.anim.slide_in_bottom
                    .addToBackStack(null)
                    .replace(R.id.fragmentHolder, new FragmentWineStores())
                    .commit();

        } else if (id == R.id.nav_settings) {
            this.setTitle(res.getString(R.string.settings));
            if (this.getSupportActionBar() != null){
                this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white);
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_from_left, R.anim.slide_in_bottom,
                            R.anim.slide_from_left, R.anim.slide_in_bottom)
                    .addToBackStack(null)
                    .replace(R.id.fragmentHolder, new FragmentSettings())
                    .commit();


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getFavorites() {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_from_left, R.anim.slide_in_bottom,
                        R.anim.slide_from_left, R.anim.slide_in_bottom)
                .addToBackStack(null)
                .replace(R.id.fragmentHolder, new FragmentFavoriteWines())
                .commit();
    }

    private void getApiData() {
        WineApi.Factory.getInstance().getDesserts(getResources().getString(R.string.wine_api_key),
                Constants.DESSERT_NAME,
                Constants.DESSERT_MIN_RANK,
                Constants.DESSERT_MAX_RANK,
                Constants.DESSERT_MIN_PRICE,
                Constants.DESSERT_MAX_PRICE,
                Constants.DESSERT_SORTBY,
                Constants.DESSERT_NUM_RESULTS).enqueue(new Callback<DessertWine>() {
            @Override
            public void onResponse(Call<DessertWine> call, Response<DessertWine> response) {
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
                        mywine.setSnoothrank((long) (wine.getSnoothrank()));
                        myWineList.add(mywine);
                    }

                    //  Send data to the MainWineActivity
                    if (myWineList.size() > 0) {
                        onWineFound(myWineList, mProgress );
                        mProgress.dismiss();
                    } else {
                        mProgress.dismiss();
                        Toast.makeText(getApplicationContext(), getString(R.string.no_results_Found), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    mProgress.dismiss();
                    ResponseBody errBody = response.errorBody();
                   // Log.d(TAG, errBody.toString());
                    Toast.makeText(getApplicationContext(), getString(R.string.no_results_Found) + errBody.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DessertWine> call, Throwable t) {
                mProgress.dismiss();
                Toast.makeText(getApplicationContext(), getString(R.string.failed_to_get_data) + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onRegionSelected(String region) {
        FragmentWineSearch fm;
        if (Constants.isTablet){
            fm = new FragmentWineSearch();

        } else {
            fm = (FragmentWineSearch) getSupportFragmentManager().findFragmentById(R.id.fragmentHolder);
        }
        fm.updateRegion(region);

    }

    @Override
    public void onWineFound(ArrayList<MyWine> wineList, ProgressDialog mProgress) {
        FragmentWineResults fragmentResults = new FragmentWineResults();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("WINELIST", wineList);
        fragmentResults.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_from_left, R.anim.slide_from_right,
                        R.anim.slide_from_left, R.anim.slide_from_right)  //R.anim.slide_in_bottom
                .addToBackStack(null)
                .replace(R.id.fragmentHolder, fragmentResults)
                .commit();

        mProgress.dismiss();
    }
}
