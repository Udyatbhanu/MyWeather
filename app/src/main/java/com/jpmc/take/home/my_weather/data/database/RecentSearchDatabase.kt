package com.jpmc.take.home.my_weather.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jpmc.take.home.my_weather.data.database.dao.RecentSearchDao

@Database(entities = [RecentSearchEntity::class], version = 1, exportSchema = false)
abstract class RecentSearchDatabase : RoomDatabase() {
    abstract fun recentSearchDao(): RecentSearchDao
}