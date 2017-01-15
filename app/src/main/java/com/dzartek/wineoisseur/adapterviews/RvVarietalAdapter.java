package com.dzartek.wineoisseur.adapterviews;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import com.dzartek.wineoisseur.R;

import java.util.ArrayList;

/**
 * Created by dzarrillo on 10/7/2016.
 */

public class RvVarietalAdapter extends RecyclerView.Adapter<RvVarietalAdapter.VarietalViewHolder>{
    private RadioButton lastCheckedRB = null;
    private ArrayList<String> varietals;
    // usersettings - varietal
    private String userVarietal;

    public RvVarietalAdapter(ArrayList<String> varietals, String userVarietal){
        this.varietals = varietals;
        this.userVarietal = userVarietal;
    }

    public static class VarietalViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        RadioButton radioButtonVarietal;

        public VarietalViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cardViewVarietal);
            radioButtonVarietal = (RadioButton) itemView.findViewById(R.id.radioButtonVarietal);
        }
    }

    @Override
    public VarietalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_varietal_cardview, parent, false);
        VarietalViewHolder vvh = new VarietalViewHolder(v);
        return vvh;
    }

    @Override
    public void onBindViewHolder(final VarietalViewHolder holder, int position) {
        holder.radioButtonVarietal.setText(varietals.get(position));


        String name = holder.radioButtonVarietal.getText().toString();

        if (name.equals(userVarietal)){
            holder.radioButtonVarietal.setChecked(true);
            lastCheckedRB = holder.radioButtonVarietal;
        }

        holder.radioButtonVarietal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (lastCheckedRB != null){
                    lastCheckedRB.setChecked(false);
                }
                // Save last clicked radiobutton
                lastCheckedRB = holder.radioButtonVarietal;
            }
        });


    }

    @Override
    public int getItemCount() {
        return varietals.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
