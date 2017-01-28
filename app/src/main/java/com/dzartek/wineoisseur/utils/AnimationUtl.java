package com.dzartek.wineoisseur.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;

/**
 * Created by dzarrillo on 1/24/2017.
 */

public class AnimationUtl {

    public static void animate(RecyclerView.ViewHolder holder, boolean goesDown){
        AnimatorSet animatorset = new AnimatorSet();
        ObjectAnimator animatorTranslateY = ObjectAnimator.ofFloat(holder.itemView, "translationY", goesDown==true ? 200 : -200, 0);
        ObjectAnimator animatorTranslateX = ObjectAnimator.ofFloat(holder.itemView, "translationX", -50, 50, -30,-20, 20, -5,5, 0);
        animatorTranslateY.setDuration(1000);
        animatorTranslateX.setDuration(1000);

//        animatorset.playTogether(animatorTranslateY);
        animatorset.playTogether(animatorTranslateX,animatorTranslateY);
        animatorset.start();

    }
}
