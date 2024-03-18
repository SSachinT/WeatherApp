package com.sachin.android.sachinapp.network

import com.sachin.android.sachinapp.models.NewWeather
import com.sachin.android.sachinapp.models.WeatherData
import com.sachin.android.sachinapp.models.fivedaysdata.ForecastResponse
import com.sachin.android.sachinapp.models.groupdata.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface OpenWeatherMapService {
    @GET("weather")
     fun getCurrentWeatherData(
        @Query("q") location: String?,
        @Query("appid") apiKey: String?
    ): Call<NewWeather?>?
    @GET("group")
    fun getMultipleCityWeatherData(
        @Query("id") cityIds: String, // Comma-separated list of city IDs
        @Query("appid") apiKey: String
    ): Call<WeatherResponse> // Define WeatherResponse based on your API response structure

    @GET("forecast")
    fun getFiveDaysWeatherData(
        @Query("id") cityId: String,
        @Query("appid") apiKey: String
    ): Call<ForecastResponse>
}