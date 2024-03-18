package com.sachin.android.sachinapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sachin.android.sachinapp.models.fivedaysdata.ForecastData

class SharedPrefManagerForForcast(context: Context) {
    private val PREF_NAME = "ForecastPrefs"
    private val KEY_FORECAST_DATA = "forecastData"

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveForecastData(cityId: String, forecastData: String) {
        val editor = sharedPreferences.edit()
        val jsonString = Gson().toJson(forecastData)
        editor.putString("$KEY_FORECAST_DATA-$cityId", jsonString)
        editor.apply()
    }

    fun getForecastData(cityId: String): List<ForecastData>? {
        val jsonString = sharedPreferences.getString("$KEY_FORECAST_DATA-$cityId", null)
        return Gson().fromJson(jsonString, object : TypeToken<List<ForecastData>>() {}.type)
    }
}

