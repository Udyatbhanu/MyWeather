package com.jpmc.take.home.my_weather.domain.geocode

import com.jpmc.take.home.my_weather.core.ResultWrapper
import com.jpmc.take.home.my_weather.data.dto.Locations
import com.jpmc.take.home.my_weather.data.geocode.SearchResultsResponse
import com.jpmc.take.home.my_weather.data.geocode.SearchResultsResponseItem
import com.jpmc.take.home.my_weather.data.weather.WeatherRequest
import kotlinx.coroutines.flow.Flow

interface SearchLocationUseCase {

    suspend fun searchLocation(query: String): ResultWrapper<Locations>

    suspend fun getRecentSearches(): ResultWrapper<Locations>
}