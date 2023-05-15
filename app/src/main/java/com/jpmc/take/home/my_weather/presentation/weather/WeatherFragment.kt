package com.jpmc.take.home.my_weather.presentation.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jpmc.take.home.my_weather.R
import com.jpmc.take.home.my_weather.core.SessionViewModel
import com.jpmc.take.home.my_weather.data.dto.Locations
import com.jpmc.take.home.my_weather.data.dto.WeatherDetails

import com.jpmc.take.home.my_weather.databinding.FragmentSearchBinding
import com.jpmc.take.home.my_weather.presentation.WeatherMainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Sealed class to track Weather Fragment State
 */
sealed class WeatherFragmentState {
    data class Success(val networkResults: Locations) : WeatherFragmentState()
    data class RecentSearches(val cachedResults: Locations) : WeatherFragmentState()
    data class CurrentWeather(val response: WeatherDetails) : WeatherFragmentState()

    object InFlight : WeatherFragmentState()
    object Error : WeatherFragmentState()
    object Idle : WeatherFragmentState()
    object Empty : WeatherFragmentState()
}

@AndroidEntryPoint
class WeatherFragment
    : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    private val sessionViewModel by activityViewModels<SessionViewModel>()

    private val weatherViewModel by viewModels<WeatherViewModel>()

    private lateinit var weatherResultsAdapter: WeatherResultsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        subscribe()

    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            weatherViewModel.loadRecentSearches()
            weatherViewModel.getWeatherAtCurrentLocation()
        }
    }


    /**
     * Initialization is used for loading ui components, on click listeners etc.
     */
    private fun initialize() {
        weatherResultsAdapter = WeatherResultsAdapter {
            lifecycleScope.launch {
                weatherViewModel.getCurrentWeather(it)
            }
        }
        binding.searchResultsList.adapter = weatherResultsAdapter


        //search view listener
        with(binding.searchView) {
            queryHint = "Location"
            isIconified = false
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(searchQuery: String?): Boolean {
                    lifecycleScope.launch {
                        if (!searchQuery.isNullOrEmpty()) {
                            weatherViewModel.searchUsingGeocode(searchQuery)
                        }
                    }
                    return false
                }

                override fun onQueryTextChange(searchQuery: String?): Boolean {
                    // show recent searches when user starts to type
                    if ((query?.length ?: 0) >= 1) {
                        changeResultsVisibility(true)
                        lifecycleScope.launch {
                            weatherViewModel.loadRecentSearches()
                        }
                    } else {
                        changeResultsVisibility(false)
                    }
                    return false
                }
            })
        }

        //clear all recent searches listener.
        binding.clearAll.setOnClickListener {
            lifecycleScope.launch {
                weatherViewModel.clearHistory()
            }
            binding.clearAll.visibility = View.GONE
        }


        //get weather at current location
        binding.yourLocation.setOnClickListener {
            (activity as WeatherMainActivity).attemptLocation()
        }


    }


    /**
     * Subscriptions are used to collect state flow and react to changes in data.
     */
    private fun subscribe() {
        lifecycleScope.launch {
            weatherViewModel.state.collect { state ->
                when (state) {
                    is WeatherFragmentState.Success -> loadResultsFromApi(state.networkResults)
                    is WeatherFragmentState.RecentSearches -> loadRecentSearches(state.cachedResults)
                    is WeatherFragmentState.CurrentWeather -> loadCurrentWeather(state.response)
                    is WeatherFragmentState.InFlight -> toggleProgressBar(true)
                    is WeatherFragmentState.Error -> loadError()
                    else -> {}
                }
            }
        }

        lifecycleScope.launch {
            sessionViewModel.locationState.collect {
                changeResultsVisibility(false)
                weatherViewModel.getWeatherAtCurrentLocation()
            }
        }
    }

    private fun toggleProgressBar(show : Boolean){
        binding.progressCircular.visibility = if (show) (View.VISIBLE) else (View.GONE)
    }


    /**
     * Load results for location searched by user.
     */
    private fun loadResultsFromApi(locations: Locations) {
        toggleProgressBar(false)
        binding.clearAll.visibility = View.GONE
        weatherResultsAdapter.submitList(locations)
    }


    /**
     * Get current weather.
     */
    private fun loadCurrentWeather(weatherDetails: WeatherDetails) {
        toggleProgressBar(false)
        changeResultsVisibility(false)
        context?.let {
            Glide.with(it)
                .load(weatherDetails.image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.weatherIcon)
        }
        binding.weatherDetails = weatherDetails
    }

    /**
     * Load recent Searches, read from room db.
     */
    private fun loadRecentSearches(locations: Locations) {
        toggleProgressBar(false)
        binding.errorLayout.visibility = View.GONE
        binding.clearAll.visibility = if (locations.isNotEmpty()) (View.VISIBLE) else (View.GONE)
        weatherResultsAdapter.submitList(locations)
    }


    /**
     * Update view visibility state.
     */
    private fun changeResultsVisibility(state: Boolean) {
        binding.errorLayout.visibility = View.GONE
        if (state) {
            binding.locationResultsLayout.visibility = View.VISIBLE
            binding.weatherParentLayout.visibility = View.GONE
        } else {
            binding.locationResultsLayout.visibility = View.GONE
            binding.weatherParentLayout.visibility = View.VISIBLE
        }
    }

    /**
     * Load an error message.
     */
    private fun loadError() {
        toggleProgressBar(false)
        binding.locationResultsLayout.visibility = View.GONE
        binding.weatherParentLayout.visibility = View.GONE
        binding.errorLayout.visibility = View.VISIBLE
        context?.let {
            Glide.with(it)
                .load(R.drawable.error)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.errorImage)
        }
    }
}