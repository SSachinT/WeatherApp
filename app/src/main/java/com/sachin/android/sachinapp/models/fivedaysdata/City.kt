package com.sachin.android.sachinapp.models.fivedaysdata

import com.sachin.android.sachinapp.models.groupdata.Coord

data class City(
    val id: Long,
    val name: String,
    val coord: Coord,
    val country: String,
    val population: Int,
    val timezone: Int,
    val sunrise: Long,
    val sunset: Long
)
