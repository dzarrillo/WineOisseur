package com.dzartek.wineoisseur.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.dzartek.wineoisseur.MainWineActivity;
import com.dzartek.wineoisseur.R;

/**
 * Implementation of App Widget functionality.
 */
public class WidgetWineProvider extends AppWidgetProvider {
    private final String TAG = WidgetWineProvider.class.getName();


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //super.onUpdate(context, appWidgetManager, appWidgetIds);
       // Log.d(TAG, "onUpdate method called");

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
         //   Log.d(TAG, "onUpdate method called inside for loop");

            updateAppWidget(context, appWidgetManager, appWidgetId);

            RemoteViews rv = new RemoteViews(context.getPackageName(),
                    R.layout.widget_wine_provider);

           // Log.d(TAG, "onUpdate method called inside for loop - before intent");

            Intent adapter = new Intent(context, WidgetWineService.class);
            adapter.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds);

          //  Log.d(TAG, "onUpdate method called inside for loop - after intent");
            //  *****
            Intent intent = new Intent(context, MainWineActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            rv.setPendingIntentTemplate(R.id.appwidget_ListView, pendingIntent);
            rv.setOnClickFillInIntent(R.id.appwidget_ListView, intent );
            //  *****

            rv.setRemoteAdapter(R.id.appwidget_ListView, adapter);

            appWidgetManager.updateAppWidget(appWidgetIds, rv);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,
                    R.id.appwidget_ListView);
        }

      //  Log.d(TAG, "onUpdate method called outside for loop");
    }


    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

       // Log.d(TAG, "updateAppWidget method ");

        //CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_wine_provider);
        views.setTextViewText(R.id.appwidget_text, context.getString(R.string.appwidget_text));
        setList(views, context, appWidgetId);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.appwidget_ListView);
    }

    void setList(RemoteViews rv, Context context, int appWidgetId) {
      //  Log.d(TAG, "setList method ");
        Intent adapter = new Intent(context, WidgetWineService.class);
        adapter.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        rv.setRemoteAdapter(R.id.appwidget_ListView, adapter);
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


}

