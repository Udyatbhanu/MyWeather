package com.jpmc.take.home.my_weather.domain.recent_searches

import com.jpmc.take.home.my_weather.core.ResultWrapper
import com.jpmc.take.home.my_weather.data.dto.Location
import com.jpmc.take.home.my_weather.data.dto.Locations


interface RecentSearchesUseCase {
    suspend fun getRecentSearches(): ResultWrapper<Locations>

    suspend fun addRecentSearch(location: Location)

    suspend fun clearAll()


}