package com.dzartek.wineoisseur.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.dzartek.wineoisseur.R;
import com.dzartek.wineoisseur.adapterviews.RvWineResultsAdapter;
import com.dzartek.wineoisseur.datamodel.MyWine;

import java.util.ArrayList;

/**
 * Created by dzarrillo on 10/13/2016.
 */

public class FragmentWineResults extends Fragment {
    private final String TAG = FragmentWineResults.class.getName();
    private ArrayList<MyWine> myWineList;
    private RecyclerView mRecyclerViewResults;
    private RvWineResultsAdapter mWineResultsAdapter;
    private GridLayoutManager mGridLayoutMgrWineResults;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            myWineList = this.getArguments().getParcelableArrayList("WINELIST");
        } else {
            myWineList = savedInstanceState.getParcelableArrayList("WINELIST");
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList("WINELIST", myWineList);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_wine_results, container, false);

        initializeRecyclerView(v);

        String screenName = getResources().getString(R.string.results);
        getActivity().setTitle(screenName);
        return v;
    }

    private void initializeRecyclerView(View v) {

        mRecyclerViewResults = (RecyclerView) v.findViewById(R.id.recyclerViewWineResults);
        mGridLayoutMgrWineResults = new GridLayoutManager(getActivity(), 1);
        mRecyclerViewResults.setLayoutManager(mGridLayoutMgrWineResults);
        mRecyclerViewResults.setHasFixedSize(true);
        mWineResultsAdapter = new RvWineResultsAdapter(getActivity(), myWineList);
        mRecyclerViewResults.setAdapter(mWineResultsAdapter);

        mRecyclerViewResults.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                mRecyclerViewResults, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
//                Toast.makeText(getActivity(), "WebSite: " + myWineList.get(position).getLink(), Toast.LENGTH_LONG).show();

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
        private FragmentWineResults.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final FragmentWineResults.ClickListener clickListener) {
          //  Log.d(TAG, "constructor invoked!");
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
          //  Log.d(TAG, "onTouchEvent!");
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }


}
