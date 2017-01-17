package com.dzartek.wineoisseur.adapterviews;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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
import com.dzartek.wineoisseur.datamodel.MyWine;
import com.dzartek.wineoisseur.service.FavoritesIntentService;
import com.dzartek.wineoisseur.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by dzarrillo on 10/14/2016.
 */

public class RvWineResultsAdapter extends RecyclerView.Adapter<RvWineResultsAdapter.WineResultViewHolder> {
    private ArrayList<MyWine> myWineList;
    private Context context;
    //private FirebaseAnalytics mFirebaseAnalytics;
    public RvWineResultsAdapter(Context context, ArrayList<MyWine> myWineList) {
        this.myWineList = myWineList;
        this.context = context;
    }


    static class WineResultViewHolder extends RecyclerView.ViewHolder {
        CardView cardviewResults;
        ImageView imageViewBottle;
        TextView textViewName, textViewPrice;
        RatingBar ratingBarWine;
        ImageButton imageButtonFavorites;

        WineResultViewHolder(View itemView) {
            super(itemView);
            cardviewResults = (CardView) itemView.findViewById(R.id.cardviewResult);
            imageViewBottle = (ImageView) itemView.findViewById(R.id.imageViewBottle);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            textViewPrice = (TextView) itemView.findViewById(R.id.textViewPrice);
            ratingBarWine = (RatingBar) itemView.findViewById(R.id.ratingBarWine);
            imageButtonFavorites = (ImageButton) itemView.findViewById(R.id.imageButtonFavorite);
        }
    }


    @Override
    public WineResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_wine_results_row, parent, false);
        WineResultViewHolder vvh = new WineResultViewHolder(v);
        return vvh;
    }

    @Override
    public void onBindViewHolder(final WineResultViewHolder holder, final int position) {

        // Populate widgets
        final Context context = holder.imageViewBottle.getContext();

        // If tablet make image size 150
        int imagesize;
        if (Constants.isTablet){
            imagesize = Constants.tabletImageSize;
        }else{
            imagesize = Constants.phoneImageSize;
        }
        // Check to see if url is empty or not - Per Picasso docs if empty url you will
        // receive a 197 error code (empty url)
        if (myWineList.get(position).getImage().isEmpty()) {
            Picasso.with(context)
                    .load(R.drawable.ic_local_bar_black_24dp)
                    .resize(imagesize, imagesize)
                    .error(R.drawable.ic_local_bar_black_24dp)
                    .into(holder.imageViewBottle);
        } else {
            //Resources res = context.getResources();

            Picasso.with(context)
                    .load(myWineList.get(position).getImage())
                    .resize(imagesize, imagesize)
                    .error(R.drawable.ic_local_bar_black_24dp)
                    .into(holder.imageViewBottle);
        }

        holder.textViewName.setText(myWineList.get(position).getName());
        holder.textViewPrice.setText(myWineList.get(position).getPrice());
        holder.ratingBarWine.setNumStars((int) myWineList.get(position).getSnoothrank());

        //do a search in the database to see if it's already a favorite
        if (isFavorite(position)) {
            holder.imageButtonFavorites.setImageResource(R.drawable.ic_favorite_black_24dp);
        } else {
            holder.imageButtonFavorites.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }

        holder.imageButtonFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (view.getId() == holder.imageButtonFavorites.getId()) {

                    // if (record found) then
                    // holder.imageButtonFavorites.setImageResource(R.drawable.ic_favorite_black_24dp);
                    //else

                    //do a search in the database to see if it's already a favorite
                    if (isFavorite(position)) {
                        // Remove from database
                        Intent i = new Intent(context, FavoritesIntentService.class);
                        i.setAction("DELETE");
                        i.putExtra("CODE", myWineList.get(position).getCode());
                        context.startService(i);

                        holder.imageButtonFavorites.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    } else {

                        // Obtain the Firebase Analytics instance.
                        //mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);

                        // Insert data into database
                        MyWine mywine;
                        mywine = myWineList.get(position);

                        Intent i = new Intent(context, FavoritesIntentService.class);
                        i.setAction("SAVE");
                        i.putExtra("MYWINE", mywine);
//                        i.putExtra("CODE", myWineList.get(position).getCode());
                        context.startService(i);

                        holder.imageButtonFavorites.setImageResource(R.drawable.ic_favorite_black_24dp);
                    }
                    // Update the appwidget here
                    int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, WidgetWineProvider.class));
                    WidgetWineProvider myWidget = new WidgetWineProvider();
                    myWidget.onUpdate(context, AppWidgetManager.getInstance(context),ids);

                }
            }
        });


        holder.cardviewResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Constants.isOnline(context)) {
                    Toast.makeText(view.getContext(),
                            context.getString(R.string.webSite_unavailable), Toast.LENGTH_LONG).show();
                } else {

                    //Toast.makeText(view.getContext(), "WebSite: " + myWineList.get(position).getLink(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(myWineList.get(position).getLink()));
                    context.startActivity(intent);
                }
            }
        });
    }


    private boolean isFavorite(int position) {

        Cursor c = context.getContentResolver().query(FavoriteWinesContract.Wine.CONTENT_URI,
                FavoriteWinesContract.Wine.PROJECTION,
                "code = ?",
                new String[]{myWineList.get(position).getCode()},
                null);

        boolean value = false;
        if (c != null) {
            if (c.getCount() > 0) {
                value = true;
            } else {
                value = false;
            }
            c.close();
        }
        return value;
    }


    @Override
    public int getItemCount() {
        return myWineList.size();
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}
