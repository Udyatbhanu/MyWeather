package com.jpmc.take.home.my_weather.core

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jpmc.take.home.my_weather.AppManager
import com.jpmc.take.home.my_weather.presentation.WeatherActions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Parcelize
data class CurrentLocation(val latitude: Double, val longitude: Double) : Parcelable


@HiltViewModel
class SessionViewModel @Inject constructor(private val appManager: AppManager) : ViewModel() {

    private val _action: MutableStateFlow<WeatherActions> =
        MutableStateFlow(WeatherActions.Idle)
    val action: StateFlow<WeatherActions>
        get() = _action


    private val _locationState = MutableSharedFlow<CurrentLocation>()
    val locationState = _locationState.asSharedFlow()

    fun initializeState(location: CurrentLocation) {
        viewModelScope.launch {
            appManager.updateLocation(location)
            _locationState.emit(location)
        }
    }


    /**
     *
     */
    fun attemptCurrentLocation() {
        viewModelScope.launch {
            _action.value = WeatherActions.AttemptCurrentLocation
        }
    }
}