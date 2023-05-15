package com.jpmc.take.home.my_weather.domain.geocode.impl

import com.jpmc.take.home.my_weather.core.ResultWrapper
import com.jpmc.take.home.my_weather.data.GeocodeRepository
import com.jpmc.take.home.my_weather.data.RecentSearchesRepository
import com.jpmc.take.home.my_weather.data.database.toLocationDTO
import com.jpmc.take.home.my_weather.data.dto.Locations
import com.jpmc.take.home.my_weather.data.geocode.toLocationDTO
import com.jpmc.take.home.my_weather.domain.geocode.SearchLocationUseCase
import javax.inject.Inject

class SearchLocationUseCaseImpl @Inject constructor(
    private val geocodeRepository: GeocodeRepository,
    private val recentSearchesRepository: RecentSearchesRepository
) : SearchLocationUseCase {

    /**
     *
     */
    override suspend fun searchLocation(query: String): ResultWrapper<Locations> {
        return when (val results = geocodeRepository.searchLocation(query)) {
            is ResultWrapper.Success -> ResultWrapper.Success(results.value.map {
                it.toLocationDTO()
            })

            is ResultWrapper.GenericError -> ResultWrapper.GenericError()
            else -> ResultWrapper.EmptyResponse
        }

    }

    /**
     *
     */
    override suspend fun getRecentSearches(): ResultWrapper<Locations> {
        return when (val results = recentSearchesRepository.getRecentSearches()) {
            is ResultWrapper.Success -> ResultWrapper.Success(results.value.map {
                it.toLocationDTO()
            })

            else -> ResultWrapper.EmptyResponse
        }
    }
}