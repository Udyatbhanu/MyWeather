package com.jpmc.take.home.my_weather.core.di

import com.jpmc.take.home.my_weather.domain.geocode.SearchLocationUseCase
import com.jpmc.take.home.my_weather.domain.geocode.impl.SearchLocationUseCaseImpl
import com.jpmc.take.home.my_weather.domain.location.GetCurrentLocationUseCase
import com.jpmc.take.home.my_weather.domain.location.impl.GetCurrentLocationUseCaseImpl
import com.jpmc.take.home.my_weather.domain.recent_searches.RecentSearchesUseCase
import com.jpmc.take.home.my_weather.domain.recent_searches.impl.RecentSearchesUseCaseImpl
import com.jpmc.take.home.my_weather.domain.weather.GetWeatherUseCase
import com.jpmc.take.home.my_weather.domain.weather.impl.GetWeatherUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class UseCaseModule {
    @Binds
    abstract fun getSearchLocationUseCase(searchLocationUseCase: SearchLocationUseCaseImpl): SearchLocationUseCase

    @Binds
    abstract fun getWeatherUseCase(getWeatherUseCase: GetWeatherUseCaseImpl): GetWeatherUseCase

    @Binds
    abstract fun getRecentSearchesUseCase(recentSearchesUseCase: RecentSearchesUseCaseImpl): RecentSearchesUseCase

    @Binds
    abstract fun getLastLocationUseCase(getCurrentLocationUseCase: GetCurrentLocationUseCaseImpl): GetCurrentLocationUseCase
}