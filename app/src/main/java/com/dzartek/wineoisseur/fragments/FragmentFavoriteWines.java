package com.dzartek.wineoisseur.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dzartek.wineoisseur.R;
import com.dzartek.wineoisseur.adapterviews.CursorFavoritesAdapter;
import com.dzartek.wineoisseur.contentprovider.FavoriteWinesContract;

/**
 * Created by dzarrillo on 10/24/2016.
 */

public class FragmentFavoriteWines extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private final String TAG = "FragmentFavoriteWines";
    private ListView listViewFavorites;
    private CursorFavoritesAdapter mCursorFavoritesAdapter;
    // Identifies a particular Loader being used in this component
    private static final int URL_LOADER = 0;
    private Cursor mFavoriteWineCursor;


    public FragmentFavoriteWines(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_favorite_wines, container, false);

        getLoaderManager().initLoader(URL_LOADER, null, this);
        initializeListView(v);

        String screenName = getResources().getString(R.string.favorites);
        getActivity().setTitle(screenName);
        return v;
    }

    private void initializeListView(View v) {
        listViewFavorites = (ListView) v.findViewById(R.id.listViewFavorteResults);
        mCursorFavoritesAdapter = new CursorFavoritesAdapter(getActivity(), mFavoriteWineCursor, URL_LOADER );
        listViewFavorites.setAdapter(mCursorFavoritesAdapter);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id){
            case URL_LOADER:
                return new CursorLoader(getActivity(),
                        FavoriteWinesContract.Wine.CONTENT_URI,
                        FavoriteWinesContract.Wine.PROJECTION,
                        null,
                        null,
                        null
                        );
            default:
                // An invalid id was passed in
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorFavoritesAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorFavoritesAdapter.swapCursor(null);
    }


}
