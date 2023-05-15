package com.jpmc.take.home.my_weather.data

import com.jpmc.take.home.my_weather.core.BaseRepository
import com.jpmc.take.home.my_weather.core.ResultWrapper
import com.jpmc.take.home.my_weather.core.network.WeatherApi
import com.jpmc.take.home.my_weather.data.weather.WeatherRequest
import com.jpmc.take.home.my_weather.data.weather.WeatherResponse
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val api: WeatherApi,
) : BaseRepository() {
    suspend fun getWeather(request: WeatherRequest): ResultWrapper<WeatherResponse> =
        invoke {
            api.getWeather(lat = request.lat, lon = request.lon)
        }


}