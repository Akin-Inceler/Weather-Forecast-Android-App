package com.example.weatherapp.model;

public class WeatherModel {

    private String cityName;
    private double temperature;
    private String description;
    private String mainWeather;
    private int humidity;
    private double windSpeed;

    public WeatherModel(String cityName,
                        double temperature,
                        String description,
                        String mainWeather,
                        int humidity,
                        double windSpeed) {

        this.cityName = cityName;
        this.temperature = temperature;
        this.description = description;
        this.mainWeather = mainWeather;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
    }

    public String getCityName() {
        return cityName;
    }

    public double getTemperature() {
        return temperature;
    }

    public String getDescription() {
        return description;
    }

    public String getMainWeather() {
        return mainWeather;
    }

    public int getHumidity() {
        return humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }
}