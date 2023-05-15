package com.jpmc.take.home.my_weather.domain.weather

import com.jpmc.take.home.my_weather.core.ResultWrapper
import com.jpmc.take.home.my_weather.data.dto.Location
import com.jpmc.take.home.my_weather.data.dto.WeatherDetails
import com.jpmc.take.home.my_weather.data.geocode.SearchResultsResponseItem
import com.jpmc.take.home.my_weather.data.weather.WeatherResponse

interface GetWeatherUseCase {
    suspend fun getWeather(location : Location) : ResultWrapper<WeatherDetails>

    suspend fun getWeatherAtLastLocation() : ResultWrapper<WeatherDetails>

    suspend fun getWeatherAtCurrentLocation() : ResultWrapper<WeatherDetails>
}