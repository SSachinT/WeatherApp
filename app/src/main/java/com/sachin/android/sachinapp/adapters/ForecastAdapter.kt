package com.sachin.android.sachinapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sachin.android.sachinapp.databinding.ForecastItemBinding
import com.sachin.android.sachinapp.models.fivedaysdata.ForecastData
import com.sachin.android.sachinapp.utils.Utils

class ForecastAdapter(private val forecastList: List<ForecastData>) :
    RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ForecastItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(forecast: ForecastData) {
            binding.todaydate.text = Utils.convertTimestampToDateTime(forecast.dt)
            binding.todayTempData.text = "${forecast.main.feelsLike} °C"
            binding.humidity.text = forecast.main.humidity.toString()
            binding.lastUpdated.text = forecast.dt_txt
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ForecastItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val forecast = forecastList[position]
        holder.bind(forecast)
    }

    override fun getItemCount(): Int {
        return forecastList.size
    }
}
