package com.example.weatherapp.controller;

import com.example.weatherapp.model.WeatherModel;
import com.example.weatherapp.network.WeatherApiClient;

public class WeatherController {

    private WeatherApiClient apiClient;

    public WeatherController() {
        apiClient = new WeatherApiClient();
    }

    public WeatherModel fetchWeather(String cityName) throws Exception {

        if (cityName == null || cityName.trim().isEmpty()) {
            throw new Exception("City name cannot be empty");
        }

        return apiClient.getWeatherData(cityName);
    }
}