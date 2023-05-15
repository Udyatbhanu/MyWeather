package com.jpmc.take.home.my_weather.domain.recent_searches.impl

import com.jpmc.take.home.my_weather.core.ResultWrapper
import com.jpmc.take.home.my_weather.data.RecentSearchesRepository
import com.jpmc.take.home.my_weather.data.database.toLocationDTO
import com.jpmc.take.home.my_weather.data.dto.Location
import com.jpmc.take.home.my_weather.data.dto.Locations
import com.jpmc.take.home.my_weather.data.dto.toLocationEntity
import com.jpmc.take.home.my_weather.domain.recent_searches.RecentSearchesUseCase
import javax.inject.Inject

class RecentSearchesUseCaseImpl @Inject constructor(private val recentSearchesRepository: RecentSearchesRepository) :
    RecentSearchesUseCase {


    /**
     * Used when the user starts to type a city.
     */
    override suspend fun getRecentSearches(): ResultWrapper<Locations> {
        return when (val result = recentSearchesRepository.getRecentSearches()) {
            is ResultWrapper.Success -> {
                ResultWrapper.Success(result.value.map {
                    it.toLocationDTO()
                })
            }
            else -> {
                ResultWrapper.EmptyResponse
            }
        }
    }

    /**
     * Cache location.
     */
    override suspend fun addRecentSearch(location: Location) {
        recentSearchesRepository.findAndUpdateOrAdd(location.toLocationEntity())
    }

    /**
     * Delete all entries.
     */
    override suspend fun clearAll() {
        recentSearchesRepository.clearAll()
    }
}