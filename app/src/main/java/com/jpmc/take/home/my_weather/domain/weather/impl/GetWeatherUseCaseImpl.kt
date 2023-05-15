package com.jpmc.take.home.my_weather.domain.weather.impl

import com.jpmc.take.home.my_weather.core.ResultWrapper
import com.jpmc.take.home.my_weather.data.WeatherRepository
import com.jpmc.take.home.my_weather.data.dto.Location
import com.jpmc.take.home.my_weather.data.dto.WeatherDetails
import com.jpmc.take.home.my_weather.data.dto.toWeatherRequest

import com.jpmc.take.home.my_weather.data.weather.toWeatherDetails
import com.jpmc.take.home.my_weather.domain.location.GetCurrentLocationUseCase
import com.jpmc.take.home.my_weather.domain.recent_searches.RecentSearchesUseCase
import com.jpmc.take.home.my_weather.domain.weather.GetWeatherUseCase
import javax.inject.Inject

class GetWeatherUseCaseImpl @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val recentSearchesUseCase: RecentSearchesUseCase,
    private val getLastLocationUseCase: GetCurrentLocationUseCase
) : GetWeatherUseCase {


    /**
     * Get weather at the selected location.
     */
    override suspend fun getWeather(location: Location): ResultWrapper<WeatherDetails> {
        recentSearchesUseCase.addRecentSearch(location)
        return when (val result = weatherRepository.getWeather(location.toWeatherRequest())) {
            is ResultWrapper.Success -> ResultWrapper.Success(result.value.toWeatherDetails())
            is ResultWrapper.GenericError -> ResultWrapper.GenericError()
            else -> ResultWrapper.EmptyResponse
        }
    }


    /**
     * Get the weather at last location.
     * Requirement 3 to be completed, we could use jetpack datastore to cache this.
     */
    override suspend fun getWeatherAtLastLocation(): ResultWrapper<WeatherDetails> {
        TODO("Not yet implemented")

    }

    override suspend fun getWeatherAtCurrentLocation(): ResultWrapper<WeatherDetails> {
        return when (val result = weatherRepository.getWeather(
            getLastLocationUseCase.getCurrentLocation().toWeatherRequest()
        )) {
            is ResultWrapper.Success -> ResultWrapper.Success(result.value.toWeatherDetails())
            is ResultWrapper.GenericError -> ResultWrapper.GenericError()
            else -> ResultWrapper.EmptyResponse
        }
    }


}