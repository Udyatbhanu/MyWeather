package com.jpmc.take.home.my_weather.data

import com.jpmc.take.home.my_weather.core.ResultWrapper
import com.jpmc.take.home.my_weather.data.database.RecentSearchEntity
import com.jpmc.take.home.my_weather.data.database.dao.RecentSearchDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecentSearchesRepository @Inject constructor(private val recentSearchDao: RecentSearchDao) {

    /**
     * Get the recent searches.
     */
    suspend fun getRecentSearches(): ResultWrapper<List<RecentSearchEntity>> {
        return try {
            withContext(Dispatchers.IO) {
                ResultWrapper.Success(recentSearchDao.getRecentSearches())
            }
        } catch (ex: Throwable) {
            ResultWrapper.EmptyResponse

        }
    }


    /**
     * Used as soon as user click on a search result.
     */
    suspend fun findAndUpdateOrAdd(entity: RecentSearchEntity): ResultWrapper<RecentSearchEntity> {
        return try {
            withContext(Dispatchers.IO) {
                val location = recentSearchDao.findByLocation(entity.formattedLocation)
                if (location == null) {
                    recentSearchDao.addEntry(entity)
                } else {
                    recentSearchDao.update(location)
                }
                ResultWrapper.Success(entity)
            }
        } catch (ex: Throwable) {
            ResultWrapper.EmptyResponse

        }
    }


    /**
     * Delete all entries
     */
    suspend fun clearAll() {
        try {
            withContext(Dispatchers.IO) {
                recentSearchDao.nukeItAll()
            }
        } catch (ex: Throwable) {
            ResultWrapper.EmptyResponse

        }
    }

}
