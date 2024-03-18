package com.sachin.android.sachinapp

import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.sachin.android.sachinapp.adapters.ForecastAdapter
import com.sachin.android.sachinapp.databinding.ActivityFiveDaysForcastBinding
import com.sachin.android.sachinapp.models.fivedaysdata.ForecastData
import com.sachin.android.sachinapp.models.fivedaysdata.ForecastResponse
import com.sachin.android.sachinapp.network.OpenWeatherMapService
import com.sachin.android.sachinapp.network.RetrofitClient
import com.sachin.android.sachinapp.utils.SharedPrefManagerForForcast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FiveDaysForcast : AppCompatActivity() {
    lateinit var binding:ActivityFiveDaysForcastBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var adapter: ForecastAdapter
    private var forcastWeatherList: List<ForecastData> = emptyList()
    private lateinit var sharedPrefManager: SharedPrefManagerForForcast
    var cityId:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFiveDaysForcastBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading Data...")
        progressDialog.setCancelable(false)
        sharedPrefManager = SharedPrefManagerForForcast(this)
        cityId = intent.getStringExtra("cityId") ?: ""
        //////////////////////////////////////////////////

        ////////////////////////////////////////////////////
        if (isNetworkAvailableOrNo()) {

            if (cityId.isNotEmpty()) {
                getFivedaysdata(cityId)
            } else {
                Toast.makeText(this, "Id not availabe", Toast.LENGTH_SHORT).show()
            }
        }
        else {
            if (cityId != null) {
                if (isDataAvailableLocally(cityId)) {
                    // Data is available locally, load it
                    loadLocalData(cityId)
                } else {
                    // Data is not available locally, show toast message
                    Toast.makeText(this, "We don't have data for this city locally", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } else {
                Toast.makeText(this, "Invalid City ID", Toast.LENGTH_SHORT).show()
                finish()
            }
            // Internet is not available, fetch data from SharedPreferences
          /*  val savedData = sharedPrefManager.getForecastData()
            if (savedData != null) {

                forcastWeatherList = savedData
                //forcastWeatherList = weatherResponse.cityWeatherList
                setUpListView()
                Toast.makeText(this, "Turn on internet to sync data2", Toast.LENGTH_SHORT).show()
            } else {
                // Handle case where there is no data in SharedPreferences
                Toast.makeText(this, "No cached data available for 5 days", Toast.LENGTH_SHORT).show()
            }*/
        }

    }

    private fun getFivedaysdata(cityId: String) {
        try {
            progressDialog.show()

            val apiService = RetrofitClient.getInstance().create(OpenWeatherMapService::class.java)
            val apiKey = "366cf8fa2eb559ac3dba76cad194a6a9" // Replace with your OpenWeatherMap API key

            val call = apiService.getFiveDaysWeatherData(cityId, apiKey)
            call.enqueue(object : Callback<ForecastResponse> {
                override fun onResponse(call: Call<ForecastResponse>, response: Response<ForecastResponse>) {
                    if (response.isSuccessful) {
                        val forecastResponse: ForecastResponse? = response.body()
                        if (forecastResponse != null) {
                            Log.d("THISDATA5", forecastResponse.toString())
                           // sharedPrefManager.saveWeatherData(forecastResponse)
                            val jsonString = Gson().toJson(forecastResponse)
                            Log.d("MYDATA", jsonString)
                            //forcastWeatherList = forecastResponse.list // Assign weather data list here
                            forcastWeatherList = filterForecastData(forecastResponse.list)
                            saveForecastDataToSharedPreferences(forcastWeatherList)
                            updateUI(forecastResponse.city.name,forecastResponse.city.country)
                            setUpListView()
                            progressDialog.dismiss()
                        }
                    } else {
                        progressDialog.dismiss()
                        Log.d("THISDATA6", response.toString())
                    }
                }
                override fun onFailure(call: Call<ForecastResponse>, t: Throwable) {
                    Log.d("THISDATA7", t.toString())
                    progressDialog.dismiss()
                }
            })
        }catch (e:Exception){
            progressDialog.dismiss()
            Log.d("THISDATA8", e.toString())
        }
    }

    private fun updateUI(city:String,countrycode:String) {
        binding.cityNamedata.text = "City name : "+city
        binding.countryCode.text = "Country Code : "+countrycode
    }

    private fun setUpListView() {

         adapter = ForecastAdapter(forcastWeatherList)
         binding.recyclerViewOfForcast.adapter = adapter
         binding.recyclerViewOfForcast.layoutManager = LinearLayoutManager(this)
         adapter.notifyDataSetChanged()
    }
    private fun filterForecastData(forecastList: List<ForecastData>): List<ForecastData> {
        val filteredList = mutableListOf<ForecastData>()
        var prevDay: String? = null

        for (data in forecastList) {
            val currentDay = data.dt_txt.substring(0, 10) // Extract date from timestamp

            if (currentDay != prevDay) {
                filteredList.add(data)
                prevDay = currentDay
            }
        }

        return filteredList
    }
    private fun saveForecastDataToSharedPreferences(weatherList: List<ForecastData>) {
        val jsonString = Gson().toJson(weatherList)
        sharedPrefManager.saveForecastData(jsonString,cityId)
    }

    private fun isNetworkAvailableOrNo(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun isDataAvailableLocally(cityId: String): Boolean {
        // Check if data for the specified city ID is available in SharedPreferences
        return sharedPrefManager.getForecastData(cityId) != null
    }

    private fun loadLocalData(cityId: String) {
        // Load data from SharedPreferences and set up RecyclerView
        val savedData = sharedPrefManager.getForecastData(cityId)
        if (savedData != null) {
            forcastWeatherList = savedData
            setUpListView()
            progressDialog.dismiss()
        } else {
            // Handle case where data is unexpectedly null
            progressDialog.dismiss()
            Toast.makeText(this, "Error loading local data", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}