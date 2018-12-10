package com.borysenko.weatherwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;


/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 04/12/18
 * Time: 00:14
 */
public class WeatherWidget extends AppWidgetProvider {

    private static final String SYNC_CLICKED    = "update_action";
    private static final String WAITING_MESSAGE = "Waiting for Data";
    public static final int httpsDelayMs = 300;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId)  {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

        views.setTextViewText(R.id.text, WAITING_MESSAGE);
        appWidgetManager.updateAppWidget(appWidgetId, views);

        String output;
        HTTPRequest thread = new HTTPRequest();
        thread.start();
        try {
            while (true) {
                Thread.sleep(300);
                if(!thread.isAlive()) {
                    output = thread.getInfoString();
                    break;
                }
            }

        } catch (Exception e) {
            output = e.toString();
        }

        views.setTextViewText(R.id.text, output);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        RemoteViews remoteViews;
        ComponentName watchWidget;

        remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
        watchWidget = new ComponentName(context, WeatherWidget.class);

        remoteViews.setOnClickPendingIntent(R.id.text, getPendingSelfIntent(context));
        appWidgetManager.updateAppWidget(watchWidget, remoteViews);

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        super.onReceive(context, intent);

        if (SYNC_CLICKED.equals(intent.getAction())) {

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            RemoteViews remoteViews;
            ComponentName watchWidget;

            remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
            watchWidget = new ComponentName(context, WeatherWidget.class);

            remoteViews.setTextViewText(R.id.text, WAITING_MESSAGE);

            appWidgetManager.updateAppWidget(watchWidget, remoteViews);

            String output;
            HTTPRequest thread = new HTTPRequest();
            thread.start();
            try {
                while (true) {
                    Thread.sleep(httpsDelayMs);
                    if(!thread.isAlive()) {
                        output = thread.getInfoString();
                        break;
                    }
                }

            } catch (Exception e) {
                output = e.toString();
            }

            remoteViews.setTextViewText(R.id.text, output);

            appWidgetManager.updateAppWidget(watchWidget, remoteViews);
        }
    }

    protected PendingIntent getPendingSelfIntent(Context context) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(WeatherWidget.SYNC_CLICKED);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}