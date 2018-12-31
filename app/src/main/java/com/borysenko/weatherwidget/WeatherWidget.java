package com.borysenko.weatherwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.List;


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
    List<Forecast> forecastList = new ArrayList<>();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId)  {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

        views.setTextViewText(R.id.date, WAITING_MESSAGE);
        appWidgetManager.updateAppWidget(appWidgetId, views);

        HTTPRequest thread = new HTTPRequest();
        thread.start();
        List<Forecast> fList = new ArrayList<>();
        try {
            while (true) {
                Thread.sleep(300);
                if(!thread.isAlive()) {
                    fList = thread.getInfoString();
                    break;
                }
            }

        } catch (Exception e) {
            Log.e("error", e.toString());
        }

        String date = fList.get(0).getDay() + "." + fList.get(0).getMonth()
                + " " + fList.get(0).getHour() + ":00";
        String temperature = fList.get(0).getTemperatureMin() + " ... "
                + fList.get(0).getTemperatureMax();
        views.setTextViewText(R.id.date, date);
        views.setTextViewText(R.id.temperature, temperature);
        views.setTextViewText(R.id.wind_speed, fList.get(0).getWindSpeed());
        views.setTextViewText(R.id.wind_direction, fList.get(0).getWindDirection());

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        RemoteViews remoteViews;
        ComponentName watchWidget;

        remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
        watchWidget = new ComponentName(context, WeatherWidget.class);

        remoteViews.setOnClickPendingIntent(R.id.date, getPendingSelfIntent(context));
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

            remoteViews.setTextViewText(R.id.date, WAITING_MESSAGE);

            appWidgetManager.updateAppWidget(watchWidget, remoteViews);

            HTTPRequest thread = new HTTPRequest();
            thread.start();
            try {
                while (true) {
                    Thread.sleep(httpsDelayMs);
                    if(!thread.isAlive()) {
                        forecastList = thread.getInfoString();
                        break;
                    }
                }

            } catch (Exception e) {
                Log.e("error", e.toString());
            }


            String date = forecastList.get(0).getDay() + "." + forecastList.get(0).getMonth()
                    + " " + forecastList.get(0).getHour() + ":00";
            String temperature = forecastList.get(0).getTemperatureMin() + " ... "
                    + forecastList.get(0).getTemperatureMax();
            remoteViews.setTextViewText(R.id.date, date);
            remoteViews.setTextViewText(R.id.temperature, temperature);
            remoteViews.setTextViewText(R.id.wind_speed, forecastList.get(0).getWindSpeed());
            remoteViews.setTextViewText(R.id.wind_direction, forecastList.get(0).getWindDirection());

            appWidgetManager.updateAppWidget(watchWidget, remoteViews);
        }
    }

    protected PendingIntent getPendingSelfIntent(Context context) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(WeatherWidget.SYNC_CLICKED);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}