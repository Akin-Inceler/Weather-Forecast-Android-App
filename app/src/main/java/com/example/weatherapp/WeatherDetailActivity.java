package com.example.weatherapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weatherapp.controller.WeatherController;
import com.example.weatherapp.model.WeatherModel;
import com.example.weatherapp.utils.NetworkUtils;

public class WeatherDetailActivity extends AppCompatActivity {

    private TextView cityText;
    private ImageView weatherIcon;
    private TextView tempText;
    private TextView descriptionText;
    private TextView humidityText;
    private TextView windText;
    private ProgressBar progressBar;
    private Button backButton;

    private WeatherController weatherController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_detail);

        cityText = findViewById(R.id.cityText);
        weatherIcon = findViewById(R.id.weatherIcon);
        tempText = findViewById(R.id.tempText);
        descriptionText = findViewById(R.id.descriptionText);
        humidityText = findViewById(R.id.humidityText);
        windText = findViewById(R.id.windText);
        progressBar = findViewById(R.id.progressBar);
        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(v -> finish());

        weatherController = new WeatherController();

        String cityName = getIntent().getStringExtra("cityName");

        clearWeatherTexts();
        cityText.setText("Loading...");
        weatherIcon.setVisibility(View.INVISIBLE);

        if (cityName == null || cityName.trim().isEmpty()) {
            progressBar.setVisibility(View.GONE);
            cityText.setText("City name missing");
            Toast.makeText(this, "City name missing", Toast.LENGTH_LONG).show();
            return;
        }

        if (!NetworkUtils.isInternetAvailable(this)) {
            progressBar.setVisibility(View.GONE);
            cityText.setText("No internet connection");
            Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
            return;
        }

        new FetchWeatherTask().execute(cityName);
    }

    private void clearWeatherTexts() {
        tempText.setText("");
        descriptionText.setText("");
        humidityText.setText("");
        windText.setText("");

        if (weatherIcon != null) {
            weatherIcon.setVisibility(View.INVISIBLE);
        }
    }

    private void setWeatherIcon(String mainWeather) {
        weatherIcon.setVisibility(View.VISIBLE);

        if (mainWeather == null) {
            weatherIcon.setImageResource(R.drawable.ic_unknown);
            return;
        }

        switch (mainWeather) {
            case "Clear":
                weatherIcon.setImageResource(R.drawable.ic_clear);
                break;

            case "Clouds":
                weatherIcon.setImageResource(R.drawable.ic_clouds);
                break;

            case "Rain":
            case "Drizzle":
            case "Thunderstorm":
                weatherIcon.setImageResource(R.drawable.ic_rain);
                break;

            case "Snow":
                weatherIcon.setImageResource(R.drawable.ic_snow);
                break;

            default:
                weatherIcon.setImageResource(R.drawable.ic_unknown);
                break;
        }
    }

    private class FetchWeatherTask extends AsyncTask<String, Void, WeatherModel> {

        private String errorMessage = "Unknown error";

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected WeatherModel doInBackground(String... strings) {
            try {
                return weatherController.fetchWeather(strings[0]);
            } catch (Exception e) {
                if (e.getMessage() != null) {
                    errorMessage = e.getMessage();
                }
                return null;
            }
        }

        @Override
        protected void onPostExecute(WeatherModel weatherModel) {
            progressBar.setVisibility(View.GONE);

            if (weatherModel == null) {
                clearWeatherTexts();
                cityText.setText("Weather not found");
                Toast.makeText(
                        WeatherDetailActivity.this,
                        "Error: " + errorMessage,
                        Toast.LENGTH_LONG
                ).show();
                return;
            }

            cityText.setText(weatherModel.getCityName());
            tempText.setText(weatherModel.getTemperature() + " °C");
            descriptionText.setText("Weather: " + weatherModel.getDescription());
            humidityText.setText("Humidity: " + weatherModel.getHumidity() + "%");
            windText.setText("Wind: " + weatherModel.getWindSpeed() + " m/s");

            setWeatherIcon(weatherModel.getMainWeather());
        }
    }
}