package com.sachin.android.sachinapp.models.groupdata

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("cnt") val count: Int,
    @SerializedName("list") val cityWeatherList: List<CityWeather>
)
