package com.example.weatherapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {
    @GET("data/2.5/weather?")
    Call<WeatherResponse> getCurrentWeatherData(@Query("lat") String lat, @Query("lon") String lon, @Query("APPID") String app_id,@Query("units") String unit);

    @GET("data/2.5/forecast?")
    Call<WeatherForecast> getForecastWeatherData(@Query("lat") String lat, @Query("lon") String lon, @Query("APPID") String app_id,@Query("units") String unit);


}