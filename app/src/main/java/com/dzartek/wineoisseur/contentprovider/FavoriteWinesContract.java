package com.dzartek.wineoisseur.contentprovider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by dzarrillo on 10/19/2016.
 */

public class FavoriteWinesContract implements BaseColumns {

    // Name of the content provider, use package name by convention so that it
    // unique on device
    public static final String CONTENT_AUTHORITY = "com.dzartek.wineoisseur";
    // A path that points to the wine table
    public static final String PATH_VERSION = "wine";
    // Construct the Base Content Uri
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Database name
    public static final String DB_NAME = "favoritewines";
/**
 Define table name
 */
    public static final class Wine {

        // Content Uri = Content Authority + Path
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_VERSION).build();

        // Use MIME type prefix android.cursor.dir/ for returning multiple
        //items
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/com.dzartek.wineoisseur.wines";

        // Use MIME type prefix android.cursor.item/ for returning a
        // single item - tablename
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/com.dzartek.wineoisseur.wine";

        // Define table name
        public static final String TABLE_NAME = "wine";
        // Define table columns
        public static final String ID = BaseColumns._ID;
        public static final String REGION = "region";
        public static final String VARIETAL = "varietal";
        public static final String NAME = "name";
        public static final String CODE = "code";
        public static final String  WINERY = "winery";
        public static final String  PRICE = "price";
        public static final String  VINTAGE = "vintage";
        public static final String LINK = "link";
        public static final String IMAGE = "image";
        public static final String SNOOTHRANK = "snoothrank";

        //  Define query for wine table
        public static final String[] PROJECTION = new String[] {
                FavoriteWinesContract.Wine.ID,
                FavoriteWinesContract.Wine.REGION,
                FavoriteWinesContract.Wine.VARIETAL,
                FavoriteWinesContract.Wine.NAME,
                FavoriteWinesContract.Wine.CODE,
                FavoriteWinesContract.Wine.WINERY,
                FavoriteWinesContract.Wine.PRICE,
                FavoriteWinesContract.Wine.VINTAGE,
                FavoriteWinesContract.Wine.LINK,
                FavoriteWinesContract.Wine.IMAGE,
                FavoriteWinesContract.Wine.SNOOTHRANK
        };
    }

}
