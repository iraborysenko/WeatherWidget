package com.borysenko.weatherwidget;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 10/12/18
 * Time: 18:58
 */
public class HTTPRequest extends Thread {
    private static final String urlString =
            "https://xml.meteoservice.ru/export/gismeteo/point/25.xml";

    String getInfoString() {
        return output;
    }

    private String output = "";

    private void requestData() {
        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            output = "output";
            List<Forecast> forecastList = XMLParser.getData(response.toString());

        } catch (Exception e) {
            Log.e("http", e.toString());
            output = e.toString();
        }
    }

    @Override
    public void run() {
        requestData();
    }
}