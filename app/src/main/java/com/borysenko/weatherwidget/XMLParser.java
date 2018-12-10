package com.borysenko.weatherwidget;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 10/12/18
 * Time: 20:59
 */
class XMLParser
{
    static void getData(String s) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput( new StringReader( s ) );
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.START_DOCUMENT) {
                    System.out.println("Start document");
                } else if(eventType == XmlPullParser.START_TAG) {
                    System.out.println("Start tag "+xpp.getName()+"\nMax "
                            + xpp.getAttributeValue(null,"max")+"\nCloudness "
                            + xpp.getAttributeValue(null,"cloudness"));
                } else if(eventType == XmlPullParser.END_TAG) {
                    System.out.println("End tag "+xpp.getName());
                } else if(eventType == XmlPullParser.TEXT) {
                    System.out.println("Text "+xpp.getText());
                }
                eventType = xpp.next();
            }
            System.out.println("End document");

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}