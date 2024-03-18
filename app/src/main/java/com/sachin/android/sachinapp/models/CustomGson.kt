package com.sachin.android.sachinapp.models

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.InstanceCreator
import com.sachin.android.sachinapp.models.NewWeather
import java.lang.reflect.Type

object CustomGson {
  /*  val gson: Gson = GsonBuilder()
        .registerTypeAdapter(NewWeather::class.java, NewWeatherInstanceCreator())
        .create()

    class NewWeatherInstanceCreator : InstanceCreator<NewWeather> {
        override fun createInstance(type: Type?): NewWeather {
            return NewWeather() // Provide your logic to create a new instance of NewWeather
        }
    }*/
}