package com.dzartek.wineoisseur.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dzartek.wineoisseur.contentprovider.FavoriteWinesContract;


/**
 * Created by dzarrillo on 10/19/2016.
 */

public class FavoriteWinesDBHelper extends SQLiteOpenHelper {

    // Database name
    private final static String DB_NAME = FavoriteWinesContract.DB_NAME;

    // Database version
    private final static int DB_VERSION = 1;

    // Table name
    private final static String TABLENAME = FavoriteWinesContract.Wine.TABLE_NAME;

    // Column names
    private final static String ID = FavoriteWinesContract.Wine.ID;
    private final static String REGION = FavoriteWinesContract.Wine.REGION;
    private final static String VARIETAL= FavoriteWinesContract.Wine.VARIETAL;
    private final static String NAME = FavoriteWinesContract.Wine.NAME;
    private final static String CODE = FavoriteWinesContract.Wine.CODE;
    private final static String WINERY = FavoriteWinesContract.Wine.WINERY;
    private final static String PRICE = FavoriteWinesContract.Wine.PRICE;
    private final static String VINTAGE = FavoriteWinesContract.Wine.VINTAGE;
    private final static String LINK = FavoriteWinesContract.Wine.LINK;
    private final static String IMAGE = FavoriteWinesContract.Wine.IMAGE;
    private final static String SNOOTHRANK = FavoriteWinesContract.Wine.SNOOTHRANK;

    // SQL statement to create wine table
    private final static String WINE_TABLE_CREATE =
            "CREATE TABLE " + TABLENAME + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + REGION + " TEXT NOT NULL,"
            + VARIETAL + " TEXT NOT NULL,"
            + NAME + " TEXT NOT NULL,"
            + CODE + " TEXT NOT NULL,"
            + WINERY + " TEXT NOT NULL,"
            + PRICE + " TEXT NOT NULL,"
            + VINTAGE + " TEXT NOT NULL,"
            + LINK + " TEXT NOT NULL,"
            + IMAGE + " TEXT NOT NULL,"
            + SNOOTHRANK + " TEXT NOT NULL"
            + ");";



    public FavoriteWinesDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create wine table
        db.execSQL(WINE_TABLE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLENAME);
        onCreate(db);

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        // By default foreign_key=off - cascading deletes will not work
        db.execSQL("PRAGMA foreign_keys=ON");
    }
}
