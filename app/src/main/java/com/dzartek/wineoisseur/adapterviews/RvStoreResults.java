package com.dzartek.wineoisseur.adapterviews;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.dzartek.wineoisseur.MapsActivity;
import com.dzartek.wineoisseur.R;
import com.dzartek.wineoisseur.datamodel.CurrentLoc;
import com.dzartek.wineoisseur.datamodel.MyStore;
import com.dzartek.wineoisseur.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by dzarrillo on 11/2/2016.
 */

public class RvStoreResults extends RecyclerView.Adapter<RvStoreResults.StoreResultViewHolder>{
    private ArrayList<MyStore> myStoreList;
    private Context context;

    public RvStoreResults(Context context, ArrayList<MyStore> myStoreList){
        this.context = context;
        this.myStoreList = myStoreList;
    }

    static class StoreResultViewHolder extends RecyclerView.ViewHolder{
        CardView cardViewStores;
        ImageView imageViewStore;
        TextView textViewName, textViewVicinity, textViewHours;


        StoreResultViewHolder(View itemView) {
            super(itemView);
            cardViewStores = (CardView) itemView.findViewById(R.id.cardViewSores);
            imageViewStore = (ImageView) itemView.findViewById(R.id.imageViewStore);
            textViewName = (TextView) itemView.findViewById(R.id.textViewStoreName);
            textViewVicinity = (TextView) itemView.findViewById(R.id.textViewVicinity);
            textViewHours = (TextView) itemView.findViewById(R.id.textViewHours);
        }
    }



    @Override
    public StoreResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_wine_stores, parent, false);
        StoreResultViewHolder vvh = new StoreResultViewHolder(v);
        return vvh;
    }


    @Override
    public void onBindViewHolder(StoreResultViewHolder holder, final int position) {
        // Populate the widgets
        final Context context = holder.imageViewStore.getContext();

        // If tablet make image size 150
        int imagesize;
        if (Constants.isTablet){
            imagesize = Constants.tabletImageSize;
        }else{
            imagesize = Constants.phoneImageSize;
        }
        // Check to see if url is empty or not - Per Picasso docs if empty url you will
        // receive a 197 error code (empty url)
        if(myStoreList.get(position).getIcon().isEmpty()){
            Picasso.with(context)
                    .load(R.drawable.ic_local_bar_black_24dp)
                    .resize(imagesize, imagesize)
                    .error(R.drawable.ic_local_bar_black_24dp)
                    .into(holder.imageViewStore);
        } else {
            Picasso.with(context)
                    .load(myStoreList.get(position).getIcon())
                    .resize(imagesize, imagesize)
                    .error(R.drawable.ic_local_bar_black_24dp)
                    .into(holder.imageViewStore);
        }

        holder.textViewName.setText(myStoreList.get(position).getName());
        holder.textViewVicinity.setText(myStoreList.get(position).getVicinity());
        holder.textViewHours.setText(myStoreList.get(position).isOpennow());

        holder.cardViewStores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // mDestination = "40.397111,-74.109785";
                String destin = myStoreList.get(position).getLatitude()
                        + "," + myStoreList.get(position).getLongitude();

                //  Show map
                Intent intent = new Intent(context, MapsActivity.class);
                intent.putExtra("ORIGIN", CurrentLoc.origin);
                intent.putExtra("DESTIN", destin);
                intent.putExtra("MODE", "walking");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myStoreList.size();
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
