package com.borysenko.weatherwidget;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 10/12/18
 * Time: 20:59
 */
class XMLParser
{
    static List<Forecast> getData(String s) {
        List<Forecast> forecastList = new ArrayList<>();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput( new StringReader( s ) );

            String month = null;
            String day = null;
            String hour = null;
            String temperatureMax = null;
            String temperatureMin = null;
            String windSpeed = null;
            String windDirection = null;

            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {

                switch (xpp.getEventType()) {
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        switch (xpp.getName()) {

                            case "FORECAST":
                                month = xpp.getAttributeValue(null,"month");
                                day = xpp.getAttributeValue(null,"day");
                                hour = xpp.getAttributeValue(null,"hour");
                                break;

                            case "TEMPERATURE":
                                temperatureMax = xpp.getAttributeValue(null,"max");
                                temperatureMin = xpp.getAttributeValue(null,"min");
                                break;

                            case "WIND":
                                windSpeed = xpp.getAttributeValue(null,"max");
                                windDirection = xpp.getAttributeValue(null,"direction");
                                break;

                            default:
                                break;
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if(xpp.getName().equals("FORECAST")) {

                            if (Integer.valueOf(temperatureMax) > 0 ) {
                                temperatureMax = "+" + temperatureMax;
                            } else if (Integer.valueOf(temperatureMax) == 0 ) {
                                temperatureMax = "0";
                            }

                            if(Integer.valueOf(temperatureMin) > 0) {
                                temperatureMin = "+" + temperatureMin;
                            } else if (Integer.valueOf(temperatureMin) == 0) {
                                temperatureMin = "0";
                            }

                            forecastList.add(new Forecast(month, day, hour,
                                    temperatureMax, temperatureMin,
                                    windSpeed, windDirection));
                        }

                    default:
                        break;
                }
                xpp.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return forecastList;
    }
}