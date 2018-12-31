package com.borysenko.weatherwidget;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 31/12/18
 * Time: 14:38
 */
public class Forecast {

    private String month;
    private String day;
    private String hour;
    private String temperatureMax;
    private String temperatureMin;
    private String windSpeed;
    private String windDirection;

    public Forecast(String month, String day, String hour,
                    String temperatureMax, String temperatureMin,
                    String windSpeed, String windDirection) {
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.temperatureMax = temperatureMax;
        this.temperatureMin = temperatureMin;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
    }

    public String getMonth() {
        return month;
    }

    public String getDay() {
        return day;
    }

    public String getHour() {
        return hour;
    }

    public String getTemperatureMax() {
        return temperatureMax;
    }

    public String getTemperatureMin() {
        return temperatureMin;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public String getWindDirection() {
        return windDirection;
    }
}
