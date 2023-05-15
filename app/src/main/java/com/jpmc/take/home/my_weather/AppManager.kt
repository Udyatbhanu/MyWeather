package com.jpmc.take.home.my_weather

import android.content.Context
import com.jpmc.take.home.my_weather.core.CurrentLocation
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow


/**
 * Central location for handling app initialization.
 */
class AppManager {
    var location: CurrentLocation? = null
    private val _locationState = MutableSharedFlow<CurrentLocation>()


    suspend fun updateLocation(currentLocation: CurrentLocation) {
        location = currentLocation
        _locationState.emit(currentLocation)
    }

}