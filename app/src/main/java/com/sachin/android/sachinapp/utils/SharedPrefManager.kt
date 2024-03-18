package com.sachin.android.sachinapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.sachin.android.sachinapp.models.groupdata.WeatherResponse

class SharedPrefManager(context: Context) {
    private val PREF_NAME = "WeatherPrefs"
    private val KEY_WEATHER_DATA = "weatherData"

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun saveWeatherData(weatherResponse: WeatherResponse) {
        val jsonString = Gson().toJson(weatherResponse)
        editor.putString(KEY_WEATHER_DATA, jsonString)
        editor.apply()
    }

    fun getWeatherData(): WeatherResponse? {
        val jsonString = sharedPreferences.getString(KEY_WEATHER_DATA, null)
        return Gson().fromJson(jsonString, WeatherResponse::class.java)
    }
}
