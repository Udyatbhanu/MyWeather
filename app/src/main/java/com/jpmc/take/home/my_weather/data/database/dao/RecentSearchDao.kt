package com.jpmc.take.home.my_weather.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jpmc.take.home.my_weather.core.Constants.Companion.LAST_SEARCH_DB
import com.jpmc.take.home.my_weather.data.database.RecentSearchEntity

@Dao
interface RecentSearchDao {

    @Query("SELECT * FROM $LAST_SEARCH_DB")
    fun getRecentSearches(): List<RecentSearchEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addEntry(searchEntity: RecentSearchEntity)


    @Query("SELECT * FROM $LAST_SEARCH_DB WHERE formattedLocation = :formattedLocation")
    suspend fun findByLocation(formattedLocation: String): RecentSearchEntity?

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(searchEntity: RecentSearchEntity)


    @Query("DELETE FROM $LAST_SEARCH_DB")
    fun nukeItAll()
}