package com.jpmc.take.home.my_weather.data

import com.jpmc.take.home.my_weather.core.BaseRepository
import com.jpmc.take.home.my_weather.core.ResultWrapper
import com.jpmc.take.home.my_weather.core.network.GeocodeApi

import com.jpmc.take.home.my_weather.data.geocode.SearchResultsResponse
import com.jpmc.take.home.my_weather.data.geocode.SearchResultsResponseItem

import javax.inject.Inject

class GeocodeRepository @Inject constructor(
    private val api: GeocodeApi,
) : BaseRepository() {


    suspend fun searchLocation(query: String): ResultWrapper<List<SearchResultsResponseItem>> =
        invoke {
            api.searchLocation(query)
        }


}