package com.sachin.android.sachinapp.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {
    fun convertTimestampToDateTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault())
        val date = Date(timestamp * 1000)
        return sdf.format(date)
    }


}