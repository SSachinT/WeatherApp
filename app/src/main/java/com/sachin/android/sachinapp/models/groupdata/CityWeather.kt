package com.sachin.android.sachinapp.models.groupdata

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city_weather_table")
data class CityWeather(
    @PrimaryKey val id: Long,
    val coord: Coord,
    val sys: Sys,
    val weather: List<Weather>,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
    val clouds: Clouds,
    val dt: Long,
    val name: String
)
