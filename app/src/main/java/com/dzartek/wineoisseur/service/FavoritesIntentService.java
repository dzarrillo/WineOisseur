package com.dzartek.wineoisseur.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import com.dzartek.wineoisseur.broadcastreceiver.FavoriteWineReceiver;
import com.dzartek.wineoisseur.contentprovider.FavoriteWinesContract;
import com.dzartek.wineoisseur.datamodel.MyWine;

import static com.dzartek.wineoisseur.utils.Constants.ACTION_DELETE;
import static com.dzartek.wineoisseur.utils.Constants.ACTION_ISFAVORITE;
import static com.dzartek.wineoisseur.utils.Constants.ACTION_SAVE;
import static com.dzartek.wineoisseur.utils.Constants.RESPONSE_MESSAGE;
import static com.dzartek.wineoisseur.utils.Constants.RESPONSE_STRING;

public class FavoritesIntentService extends IntentService {
    private String mResponseMessage = "";


    public FavoritesIntentService() {
        super("FavoritesIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            final String action = intent.getAction();
            final String name = intent.getStringExtra("CODE");

            if (    ACTION_DELETE.equals(action)) {
                handleActionDelete(name);
            } else if (ACTION_ISFAVORITE.equals(action)) {
                handleActionIsFavorite(name);
            } else if (ACTION_SAVE.equals(action)) {
                handleActionSave(intent);
            }
        }
    }

    private void handleActionIsFavorite(String name) {

        //boolean favorite = false;

        Cursor c = getApplication().getApplicationContext().getContentResolver().query(FavoriteWinesContract.Wine.CONTENT_URI,
                FavoriteWinesContract.Wine.PROJECTION,
                "code = ?",
                new String[]{name},
                null);


        if (c != null) {
            if (c.getCount() > 0) {
                // result_ok
                //favorite = true;
                mResponseMessage = "true";

            } else {
                mResponseMessage = "false";
            }
            c.close();

            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(FavoriteWineReceiver.PROCESS_RESPONSE);
            broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
            broadcastIntent.putExtra(RESPONSE_STRING, ACTION_ISFAVORITE);
            broadcastIntent.putExtra(RESPONSE_MESSAGE, mResponseMessage);
            sendBroadcast(broadcastIntent);
        }
    }


    private void handleActionDelete(String code) {

        int value = getApplication().getApplicationContext().getContentResolver().delete(FavoriteWinesContract.Wine.CONTENT_URI,
                "code = ?", new String[]{code});

        if (value > 0) {
            mResponseMessage = "Record Deleted!";
        } else {
            mResponseMessage = "Record not Deleted!";
        }

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(FavoriteWineReceiver.PROCESS_RESPONSE);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra(RESPONSE_STRING, ACTION_DELETE);
        broadcastIntent.putExtra(RESPONSE_MESSAGE, mResponseMessage);
        sendBroadcast(broadcastIntent);


    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionSave(Intent intent) {

        //final String name = intent.getStringExtra("CODE");
        MyWine mywine;
        mywine = intent.getParcelableExtra("MYWINE");

        // Insert data into database
        ContentValues newValues = new ContentValues();
        newValues.put(FavoriteWinesContract.Wine.REGION, mywine.getRegion());
        newValues.put(FavoriteWinesContract.Wine.VARIETAL, mywine.getVarietal());
        newValues.put(FavoriteWinesContract.Wine.NAME, mywine.getName());
        newValues.put(FavoriteWinesContract.Wine.CODE, mywine.getCode());
        newValues.put(FavoriteWinesContract.Wine.WINERY, mywine.getWinery());
        newValues.put(FavoriteWinesContract.Wine.PRICE, mywine.getPrice());
        newValues.put(FavoriteWinesContract.Wine.VINTAGE, mywine.getVintage());
        newValues.put(FavoriteWinesContract.Wine.LINK, mywine.getLink());
        newValues.put(FavoriteWinesContract.Wine.IMAGE, mywine.getImage());
        newValues.put(FavoriteWinesContract.Wine.SNOOTHRANK, mywine.getSnoothrank());

        Uri newUri = getApplication().getApplicationContext().getContentResolver().
                insert(FavoriteWinesContract.Wine.CONTENT_URI, newValues);

        mResponseMessage = "Record saved!";

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(FavoriteWineReceiver.PROCESS_RESPONSE);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra(RESPONSE_STRING, ACTION_SAVE);
        broadcastIntent.putExtra(RESPONSE_MESSAGE, mResponseMessage);
        sendBroadcast(broadcastIntent);
    }
}
