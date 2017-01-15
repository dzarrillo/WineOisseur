package com.dzartek.wineoisseur.appwidget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.dzartek.wineoisseur.R;
import com.dzartek.wineoisseur.contentprovider.FavoriteWinesContract;

/**
 * Created by dzarrillo on 11/9/2016.
 */

public class WidgetWineFactory implements RemoteViewsService.RemoteViewsFactory {
    private String TAG = "WidgetWineFactory";
    private Context mContext;
    private int mWidgetId;
    private Cursor mCursor;

    public WidgetWineFactory(Context context, Intent intent) {
       // Log.d(TAG, "in constructor");
        mContext = context;
        mWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }


    @Override
    public void onCreate() {
        //Nothing to do
       // Log.d(TAG, "onCreate");
    }

    @Override
    public void onDataSetChanged() {
        //Log.d(TAG, "onDataSetChanged method");

        if (mCursor != null) {
            mCursor.close();
        }

        //query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortorder)
        if (mContext != null) {

            mCursor = mContext.getContentResolver().query(FavoriteWinesContract.Wine.CONTENT_URI,
                    FavoriteWinesContract.Wine.PROJECTION,
                    null,
                    null,
                    null,
                    null);
            if (mCursor != null) {
               // Log.d(TAG, "onDataSetChanged method: recs - " + mCursor.getCount());
            } else {
               // Log.d(TAG, "onDataSetChanged method: recs - 0 ");
            }
        } else {

           // Log.d(TAG, "mContext is null");

        }

    }

    @Override
    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
        }
    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int i) {
       // Log.d(TAG, "getViewAt");
        // Populate widgets
        RemoteViews remoteviews = new RemoteViews(mContext.getPackageName(), R.layout.widget_custom_row);
        if (mCursor.moveToPosition(i)) {
            remoteviews.setTextViewText(R.id.textWidgetName,
                    mCursor.getString(mCursor.getColumnIndex(FavoriteWinesContract.Wine.NAME)));
            remoteviews.setTextViewText(R.id.textWidgetPrice,
                    mCursor.getString(mCursor.getColumnIndex(FavoriteWinesContract.Wine.PRICE)));
            remoteviews.setTextViewText(R.id.ratingBarTextWidget,
                    mCursor.getString(mCursor.getColumnIndex(FavoriteWinesContract.Wine.SNOOTHRANK)));

            //  ****
            Intent fillInIntent = new Intent();
            remoteviews.setOnClickFillInIntent(R.id.textWidgetName, fillInIntent);
        }

        return remoteviews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
