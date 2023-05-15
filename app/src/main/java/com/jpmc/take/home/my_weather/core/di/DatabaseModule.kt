package com.jpmc.take.home.my_weather.core.di

import android.content.Context
import androidx.room.Room
import com.jpmc.take.home.my_weather.core.Constants.Companion.LAST_SEARCH_DB
import com.jpmc.take.home.my_weather.data.database.RecentSearchDatabase
import com.jpmc.take.home.my_weather.data.database.dao.RecentSearchDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {


    /**
     * Provide the recent search dao
     */
    @Provides
    fun provideListsDao(recentSearchDatabase: RecentSearchDatabase): RecentSearchDao {
        return recentSearchDatabase.recentSearchDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): RecentSearchDatabase {
        return Room.databaseBuilder(
            appContext,
            RecentSearchDatabase::class.java,
            LAST_SEARCH_DB
        ).fallbackToDestructiveMigration().build()
    }
}