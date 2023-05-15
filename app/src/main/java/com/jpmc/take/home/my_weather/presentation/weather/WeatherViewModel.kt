package com.jpmc.take.home.my_weather.presentation.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jpmc.take.home.my_weather.core.ResultWrapper
import com.jpmc.take.home.my_weather.data.dto.Location
import com.jpmc.take.home.my_weather.domain.geocode.SearchLocationUseCase
import com.jpmc.take.home.my_weather.domain.recent_searches.RecentSearchesUseCase
import com.jpmc.take.home.my_weather.domain.weather.GetWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * The ViewModel just exposes data as a StateFlow which is consumed by the WeatherFragment.
 */
@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val searchLocationUseCase: SearchLocationUseCase,
    private val recentSearchesUseCase: RecentSearchesUseCase,
    private val getWeatherUseCase: GetWeatherUseCase,
) : ViewModel() {

    private val _state: MutableStateFlow<WeatherFragmentState> =
        MutableStateFlow(WeatherFragmentState.Idle)
    val state: StateFlow<WeatherFragmentState>
        get() = _state


    private fun showProgressBar(){
        viewModelScope.launch { _state.emit(WeatherFragmentState.InFlight) }
    }
    fun loadRecentSearches() {
        showProgressBar()
        viewModelScope.launch {
            _state.value = when (val cached = searchLocationUseCase.getRecentSearches()) {
                is ResultWrapper.Success -> WeatherFragmentState.RecentSearches(cached.value)
                else -> WeatherFragmentState.Empty
            }
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            recentSearchesUseCase.clearAll()
            _state.emit(WeatherFragmentState.RecentSearches(emptyList()))
        }

    }

    fun searchUsingGeocode(query: String) {
        showProgressBar()
        viewModelScope.launch {
            _state.value = when (val result = searchLocationUseCase.searchLocation(query)) {
                is ResultWrapper.Success -> WeatherFragmentState.Success(result.value)
                else -> {
                    WeatherFragmentState.Error
                }
            }
        }
    }


    fun getCurrentWeather(location: Location) {
        showProgressBar()
        viewModelScope.launch {
            _state.value = when (val result = getWeatherUseCase.getWeather(location)) {
                is ResultWrapper.Success -> WeatherFragmentState.CurrentWeather(result.value)
                else -> {
                    WeatherFragmentState.Error
                }
            }
        }
    }


    fun getWeatherAtCurrentLocation() {
        showProgressBar()
        viewModelScope.launch {
            _state.value = when (val result = getWeatherUseCase.getWeatherAtCurrentLocation()) {
                is ResultWrapper.Success -> WeatherFragmentState.CurrentWeather(result.value)
                else -> {
                    WeatherFragmentState.Error
                }
            }
        }
    }
}