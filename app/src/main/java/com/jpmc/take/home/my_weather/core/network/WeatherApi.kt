package com.jpmc.take.home.my_weather.core.network

import com.jpmc.take.home.my_weather.data.weather.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET(Endpoints.GET_WEATHER)
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "Imperial",
        @Query("appid") appId: String = ApiKeys.API_KEY
    ): WeatherResponse
}