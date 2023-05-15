package com.jpmc.take.home.my_weather.core.di

import android.content.Context
import com.jpmc.take.home.my_weather.AppManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CoreModule {


    @Singleton
    @Provides
    fun provideAppManager(): AppManager {
        return AppManager()
    }
}