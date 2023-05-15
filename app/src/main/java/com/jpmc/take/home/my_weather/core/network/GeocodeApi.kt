package com.jpmc.take.home.my_weather.core.network

import com.jpmc.take.home.my_weather.core.network.ApiKeys.API_KEY
import com.jpmc.take.home.my_weather.data.geocode.SearchResultsResponse
import com.jpmc.take.home.my_weather.data.geocode.SearchResultsResponseItem
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * The geo code api, used to search a user query
 */
interface GeocodeApi {

    @GET(Endpoints.SEARCH_LOCATION)
    suspend fun searchLocation(
        @Query("q") query: String,
        @Query("limit") limit: Int =  5,
        @Query("appid") appid: String = API_KEY
    ) : List<SearchResultsResponseItem>
}