package com.sachin.android.sachinapp.models.fivedaysdata

data class ForecastResponse(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<ForecastData>,
    val city: City
)
