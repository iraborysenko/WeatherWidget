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

        views.setTextViewText(R.id.date, getDateTitle(fList.get(0).getDay(),
                fList.get(0).getMonth(), fList.get(0).getHour()));
        views.setTextViewText(R.id.temperature, getTempetatureTitle(fList.get(0).getTemperatureMin(),
                fList.get(0).getTemperatureMax()));
        views.setTextViewText(R.id.wind_speed, fList.get(0).getWindSpeed() + " м/с");
        views.setTextViewText(R.id.wind_direction, getWindTitle(fList.get(0).getWindDirection()));

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

            remoteViews.setTextViewText(R.id.date, getDateTitle(forecastList.get(0).getDay(),
                    forecastList.get(0).getMonth(),forecastList.get(0).getHour()));
            remoteViews.setTextViewText(R.id.temperature,
                    getTempetatureTitle(forecastList.get(0).getTemperatureMin(),
                            forecastList.get(0).getTemperatureMax()));
            remoteViews.setTextViewText(R.id.wind_speed, forecastList.get(0).getWindSpeed() + " м/c");
            remoteViews.setTextViewText(R.id.wind_direction,
                    getWindTitle(forecastList.get(0).getWindDirection()));

            appWidgetManager.updateAppWidget(watchWidget, remoteViews);
        }
    }

    protected PendingIntent getPendingSelfIntent(Context context) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(WeatherWidget.SYNC_CLICKED);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    private static String getTempetatureTitle(String temperatureMin, String temperatureMax) {
        return " " + temperatureMin + " ... " + temperatureMax + " \u2103";
    }

    private static String getDateTitle(String day, String month, String hour) {
        return day + "." + month + " " + hour + ":00";
    }

    private static String getWindTitle(String windDirection) {
        switch(windDirection) {
            case "0": windDirection = "северный";
                break;
            case "1": windDirection = "северо-восточный";
                break;
            case "2": windDirection = "восточный";
                break;
            case "3": windDirection = "юго-восточный";
                break;
            case "4": windDirection = "южный";
                break;
            case "5": windDirection = "юго-западный";
                break;
            case "6": windDirection = "западный";
                break;
            case "7": windDirection = "северо-западный";
                break;
            default: windDirection = "no data";
                break;
        }
        return windDirection;
    }


}