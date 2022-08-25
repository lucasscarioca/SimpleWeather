package com.oak.simpleweather.repositories

import android.util.Log
import com.oak.simpleweather.data.DataOrException
import com.oak.simpleweather.model.Weather
import com.oak.simpleweather.model.WeatherObject
import com.oak.simpleweather.network.WeatherApi
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val api: WeatherApi) {
    suspend fun getWeather(cityQuery: String, units: String = "metric"): DataOrException<Weather, Boolean, Exception> {
        val response = try {
            api.getWeather(query = cityQuery, units = units)

        }catch (e: Exception) {
            return DataOrException(e = e)
        }
        return DataOrException(data = response)
    }
}