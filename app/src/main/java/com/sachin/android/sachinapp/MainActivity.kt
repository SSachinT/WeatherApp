package com.sachin.android.sachinapp

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.sachin.android.sachinapp.adapters.WeatherAdapter
import com.sachin.android.sachinapp.databinding.ActivityMainBinding
import com.sachin.android.sachinapp.models.groupdata.CityWeather
import com.sachin.android.sachinapp.models.groupdata.WeatherResponse
import com.sachin.android.sachinapp.network.OpenWeatherMapService
import com.sachin.android.sachinapp.network.RetrofitClient
import com.sachin.android.sachinapp.utils.SharedPrefManager
import retrofit2.Response
import retrofit2.Call
import retrofit2.Callback


class MainActivity : AppCompatActivity() {
    private lateinit var openWeatherMapService: OpenWeatherMapService
    private val apiKey = "366cf8fa2eb559ac3dba76cad194a6a9"
    private lateinit var progressDialog: ProgressDialog
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: WeatherAdapter
    private lateinit var sharedPrefManager: SharedPrefManager
    //private lateinit var weatherList: List<CityWeather>
    private var weatherList: List<CityWeather> = emptyList()// Assume you have the list of weather data
    val cityIds = "1272508,1260086,1258972,1260607,1271715,1274746,1256237,1258526,1277333,1275841,1275339,1269771,1256523,1279186,1266366,1269515,1264527,1264733,1275004"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPrefManager = SharedPrefManager(this)
        //setupRecyclerView()
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading Data...")
        progressDialog.setCancelable(false)
        if (isNetworkAvailable()) {
            // Internet is available, fetch data from API
           // val cityIds = "your_city_ids_here"
            getWeatherForMultipleCities(cityIds)
        } else {
            // Internet is not available, fetch data from SharedPreferences
            val weatherResponse = sharedPrefManager.getWeatherData()
            if (weatherResponse != null) {
                weatherList = weatherResponse.cityWeatherList
                setupRecyclerView()
                Toast.makeText(this, "Turn on internet to sync data", Toast.LENGTH_SHORT).show()
            } else {
                // Handle case where there is no data in SharedPreferences
                Toast.makeText(this, "No cached data available", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun getWeatherForMultipleCities(cityIds: String) {
        try {
            progressDialog.show()

            val apiService = RetrofitClient.getInstance().create(OpenWeatherMapService::class.java)
            val apiKey = "366cf8fa2eb559ac3dba76cad194a6a9" // Replace with your OpenWeatherMap API key

            val call = apiService.getMultipleCityWeatherData(cityIds, apiKey)
            call.enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                    if (response.isSuccessful) {
                        val weatherResponse: WeatherResponse? = response.body()
                        if (weatherResponse != null) {
                            Log.d("THISDATA5", weatherResponse.toString())
                            sharedPrefManager.saveWeatherData(weatherResponse)
                            val jsonString = Gson().toJson(weatherResponse)
                            Log.d("MYDATA", jsonString)
                            weatherList = weatherResponse.cityWeatherList // Assign weather data list here
                            setupRecyclerView()
                            progressDialog.dismiss()
                        }
                    } else {
                        progressDialog.dismiss()
                        Log.d("THISDATA6", response.toString())
                    }
                }
                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    Log.d("THISDATA7", t.toString())
                    progressDialog.dismiss()
                }
            })
        }catch (e:Exception){
            progressDialog.dismiss()
            Log.d("THISDATA8", e.toString())
        }
    }


    private fun setupRecyclerView() {
        adapter = WeatherAdapter(weatherList) { position ->
            // Handle item click here
            val cityId = weatherList[position].id
            Log.d("THISDATA12", cityId.toString())
            val intent = Intent(this@MainActivity, FiveDaysForcast::class.java)
            intent.putExtra("cityId", cityId.toString())
            startActivity(intent)
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter.notifyDataSetChanged()
       /* adapter = WeatherAdapter(weatherList)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter.notifyDataSetChanged()*/
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }


}