package com.dzartek.wineoisseur.appwidget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by dzarrillo on 11/10/2016.
 */

public class WidgetWineService extends RemoteViewsService {
    private final String TAG = WidgetWineService.class.getName();
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
       // Log.d(TAG, "WidgetWineService - RemoteViewsFactory");
        return new WidgetWineFactory(getApplicationContext(), intent);
    }
}
