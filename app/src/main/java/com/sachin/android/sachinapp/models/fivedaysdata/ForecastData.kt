package com.sachin.android.sachinapp.models.fivedaysdata

import com.sachin.android.sachinapp.models.groupdata.Clouds
import com.sachin.android.sachinapp.models.groupdata.Main
import com.sachin.android.sachinapp.models.groupdata.Sys
import com.sachin.android.sachinapp.models.groupdata.Weather
import com.sachin.android.sachinapp.models.groupdata.Wind

data class ForecastData(
    val dt: Long,
    val main: Main,
    val weather: List<Weather>,
    val clouds: Clouds,
    val wind: Wind,
    val visibility: Long,
    val pop: Double,
    val sys: Sys,
    val dt_txt: String
)
