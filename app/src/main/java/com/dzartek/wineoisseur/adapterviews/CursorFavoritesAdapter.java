package com.dzartek.wineoisseur.adapterviews;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dzartek.wineoisseur.R;
import com.dzartek.wineoisseur.appwidget.WidgetWineProvider;
import com.dzartek.wineoisseur.contentprovider.FavoriteWinesContract;
import com.dzartek.wineoisseur.utils.Constants;
import com.squareup.picasso.Picasso;


/**
 * Created by dzarrillo on 10/24/2016.
 */

public class  CursorFavoritesAdapter extends CursorAdapter {
    private Context context;
    private Cursor mCursor;

    public CursorFavoritesAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        this.context = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.custom_wine_results_row, parent, false);
    }


    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        CardView cardView;
        ImageButton imageButtonFavorites;
        TextView textViewName, textViewPrice;
        RatingBar ratingBarWine;
        ImageView imageViewBottle;

        imageViewBottle = (ImageView) view.findViewById(R.id.imageViewBottle);
        textViewName = (TextView) view.findViewById(R.id.textViewName);
        textViewPrice = (TextView) view.findViewById(R.id.textViewPrice);
        ratingBarWine = (RatingBar) view.findViewById(R.id.ratingBarWine);
        imageButtonFavorites = (ImageButton) view.findViewById(R.id.imageButtonFavorite);
        imageButtonFavorites.setTag(cursor.getPosition());


        // If tablet make image size 150
        int imagesize;
        if (Constants.isTablet){
            imagesize = Constants.tabletImageSize;
        }else{
            imagesize = Constants.phoneImageSize;
        }

        String imageBottle = cursor.getString(cursor.getColumnIndex(FavoriteWinesContract.Wine.IMAGE));
        if (imageBottle.isEmpty()) {
            Picasso.with(context)
                    .load(R.drawable.ic_local_bar_black_24dp)
                    .resize(imagesize, imagesize)
                    .error(R.drawable.ic_local_bar_black_24dp)
                    .into(imageViewBottle);
        } else {
            Picasso.with(context)
                    .load(imageBottle)
                    .resize(imagesize, imagesize)
                    .error(R.drawable.ic_local_bar_black_24dp)
                    .into(imageViewBottle);
        }


        String name = cursor.getString(cursor.getColumnIndex(FavoriteWinesContract.Wine.NAME));
        textViewName.setText(name);

        String price = cursor.getString(cursor.getColumnIndex(FavoriteWinesContract.Wine.PRICE));
        textViewPrice.setText(price);

        String snoothrank = cursor.getString(cursor.getColumnIndex(FavoriteWinesContract.Wine.SNOOTHRANK));
        ratingBarWine.setNumStars(Integer.parseInt(snoothrank));

        mCursor = cursor;

        imageButtonFavorites.setImageResource(R.drawable.ic_favorite_black_24dp);
        imageButtonFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int gid = (int) view.getTag();

                mCursor.moveToPosition(gid);
                String value = mCursor.getString(mCursor.getColumnIndex(FavoriteWinesContract.Wine.NAME));
                String code = mCursor.getString(mCursor.getColumnIndex(FavoriteWinesContract.Wine.CODE));
                int result = context.getContentResolver().delete(FavoriteWinesContract.Wine.CONTENT_URI,
                        "code = ?",
                        new String[]{code});
                if (result > 0) {
                    Toast.makeText(mContext, value + " has been removed!", Toast.LENGTH_SHORT).show();
                    // Update the appwidget here
                    int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, WidgetWineProvider.class));
                    WidgetWineProvider myWidget = new WidgetWineProvider();
                    myWidget.onUpdate(context, AppWidgetManager.getInstance(context),ids);
                } else {
                    Toast.makeText(mContext, value + " has not been deleted!", Toast.LENGTH_SHORT).show();
                }

            }
        });


        cardView = (CardView) view.findViewById(R.id.cardviewResult);
        cardView.setTag(cursor.getPosition());
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String link = mCursor.getString(mCursor.getColumnIndex(FavoriteWinesContract.Wine.LINK));

                if (!Constants.isOnline(context)) {
                    Toast.makeText(view.getContext(),
                            "WebSite: " + link
                                    + "\nis unavailable at this time!", Toast.LENGTH_LONG).show();
                } else {
                    int gid = (int) view.getTag();
                    mCursor.moveToPosition(gid);
                    link = mCursor.getString(mCursor.getColumnIndex(FavoriteWinesContract.Wine.LINK));
                    String value = mCursor.getString(mCursor.getColumnIndex(FavoriteWinesContract.Wine.NAME));
                    Toast.makeText(mContext, "Name: " + value, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                    context.startActivity(intent);
                }


            }
        });

    }

}
