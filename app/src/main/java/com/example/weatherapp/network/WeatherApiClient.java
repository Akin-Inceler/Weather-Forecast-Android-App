package com.example.weatherapp.network;

import com.example.weatherapp.model.WeatherModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class WeatherApiClient {

    private static final String API_KEY = "0eb160e61143b83fc7266513b807922d";
    private static final String BASE_URL =
            "https://api.openweathermap.org/data/2.5/weather?q=";

    public WeatherModel getWeatherData(String cityName) throws Exception {

        String encodedCityName = URLEncoder.encode(cityName, "UTF-8");

        String urlString =
                BASE_URL +
                        encodedCityName +
                        "&appid=" +
                        API_KEY +
                        "&units=metric";

        URL url = new URL(urlString);

        HttpURLConnection connection =
                (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);

        int responseCode = connection.getResponseCode();

        if (responseCode == 404) {
            throw new Exception("City not found");
        }

        if (responseCode == 401) {
            throw new Exception("Invalid API key");
        }

        if (responseCode != 200) {
            throw new Exception("API error. Code: " + responseCode);
        }

        BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(connection.getInputStream())
                );

        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();

        JSONObject jsonObject =
                new JSONObject(response.toString());

        String city =
                jsonObject.getString("name");

        JSONObject mainObject =
                jsonObject.getJSONObject("main");

        double temperature =
                mainObject.getDouble("temp");

        int humidity =
                mainObject.getInt("humidity");

        JSONArray weatherArray =
                jsonObject.getJSONArray("weather");

        String description =
                weatherArray
                        .getJSONObject(0)
                        .getString("description");

        String mainWeather =
                weatherArray
                        .getJSONObject(0)
                        .getString("main");

        JSONObject windObject =
                jsonObject.getJSONObject("wind");

        double windSpeed =
                windObject.getDouble("speed");

        connection.disconnect();

        return new WeatherModel(
                city,
                temperature,
                description,
                mainWeather,
                humidity,
                windSpeed
        );
    }
}