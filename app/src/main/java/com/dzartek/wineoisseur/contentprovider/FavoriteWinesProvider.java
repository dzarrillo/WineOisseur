package com.dzartek.wineoisseur.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.dzartek.wineoisseur.database.FavoriteWinesDBHelper;

public class FavoriteWinesProvider extends ContentProvider {
    private static final int VERSION = 100;
    private static final int VERSION_ID = 200;

    private static final UriMatcher sUriMatcher = createUriMatcher();
    private FavoriteWinesDBHelper mDbHelper;


    private static UriMatcher createUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FavoriteWinesContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, FavoriteWinesContract.PATH_VERSION, VERSION);
        uriMatcher.addURI(authority, FavoriteWinesContract.PATH_VERSION + "/#", VERSION_ID);

        return uriMatcher;
    }


    public FavoriteWinesProvider() {

    }

    @Override
    public boolean onCreate() {
        mDbHelper = new FavoriteWinesDBHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortorder) {

        // Use SQLiteQueryBuilder for querying db
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // Set the tablename
        queryBuilder.setTables(FavoriteWinesContract.Wine.TABLE_NAME);

        // record id
        String id;

        // match uri pattern
        int uriType = sUriMatcher.match(uri);

        switch (uriType){
            case VERSION:
                break;
            case VERSION_ID:
                
                selection = FavoriteWinesContract.Wine.CODE + " = ? ";
                id = uri.getLastPathSegment();
                selectionArgs = new String[] {id};
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = queryBuilder.query(
                db,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortorder
        );

        if(getContext() != null){
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)){
            case VERSION:
                return FavoriteWinesContract.Wine.CONTENT_TYPE;
            case VERSION_ID:
                return FavoriteWinesContract.Wine.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int uriType = sUriMatcher.match(uri);
        long rowId;

        switch (uriType) {
            case VERSION:
                rowId = db.insert(FavoriteWinesContract.Wine.TABLE_NAME, null, contentValues);
                if(getContext()!=null){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return ContentUris.withAppendedId(FavoriteWinesContract.Wine.CONTENT_URI, rowId);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int uriType = sUriMatcher.match(uri);
        int deletionCount;

        switch (uriType) {
            case VERSION:
                deletionCount = db.delete(FavoriteWinesContract.Wine.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        if (getContext()!=null){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return deletionCount;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String s, String[] strings) {
        // Not being implemented at this time.
        return 0;
    }


}
