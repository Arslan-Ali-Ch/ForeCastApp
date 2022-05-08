package com.example.forecastapp;

public class modelclass {
String image;
String timezone;
Long date;
String weather;
Double temp;

    public modelclass() {
    }

    public modelclass(String image, String timezone, Long date, String weather, Double temp) {
        this.image = image;
        this.timezone = timezone;
        this.date = date;
        this.weather = weather;
        this.temp = temp;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }
}

