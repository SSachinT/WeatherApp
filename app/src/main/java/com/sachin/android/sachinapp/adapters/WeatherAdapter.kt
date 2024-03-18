package com.sachin.android.sachinapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sachin.android.sachinapp.databinding.ItemWeatherBinding

import com.sachin.android.sachinapp.models.groupdata.CityWeather
import com.sachin.android.sachinapp.utils.Utils

class WeatherAdapter(
    private val weatherList: List<CityWeather>,
    private val onItemClick: (position: Int) -> Unit
) : RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemWeatherBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                onItemClick(adapterPosition)
            }
        }
        fun bind(cityWeather: CityWeather) {
            binding.cityName.text = cityWeather.name
            binding.currentTemp.text = "${cityWeather.main.feelsLike} °C"
            binding.todayHighTemp.text = cityWeather.main.tempMax.toString()
            binding.todayLowTemp.text = cityWeather.main.tempMin.toString()
            binding.updatedOn.text = Utils.convertTimestampToDateTime(cityWeather.dt)
            // Bind other weather data as needed
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cityWeather = weatherList[position]
        holder.bind(cityWeather)
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }
}
