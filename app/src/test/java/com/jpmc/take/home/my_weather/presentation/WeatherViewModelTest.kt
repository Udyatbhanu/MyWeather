package com.jpmc.take.home.my_weather.presentation

import com.jpmc.take.home.my_weather.MainDispatcherRule
import com.jpmc.take.home.my_weather.core.ResultWrapper
import com.jpmc.take.home.my_weather.data.dto.Location
import com.jpmc.take.home.my_weather.data.dto.WeatherDetails
import com.jpmc.take.home.my_weather.domain.geocode.SearchLocationUseCase
import com.jpmc.take.home.my_weather.domain.recent_searches.RecentSearchesUseCase
import com.jpmc.take.home.my_weather.domain.weather.GetWeatherUseCase
import com.jpmc.take.home.my_weather.presentation.weather.WeatherFragmentState
import com.jpmc.take.home.my_weather.presentation.weather.WeatherViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain

import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class WeatherViewModelTest {

    // Fake data to test.
    private val fakeRecentSearchResults = listOf(
        Location(
            lat = 0.0,
            lon = 0.0,
            name = "Concord",
            state = "CA",
            formattedLocation = "Concord, CA"
        )
    )

    private val fakeGeoLocationSearchResults = listOf(
        Location(
            lat = 0.0,
            lon = 0.0,
            name = "Concord",
            state = "CA",
            formattedLocation = "Concord, CA"
        ),
        Location(
            lat = 0.0,
            lon = 0.0,
            name = "Concord",
            state = "CA",
            formattedLocation = "Concord, CA"
        ),
        Location(
            lat = 0.0,
            lon = 0.0,
            name = "Concord",
            state = "CA",
            formattedLocation = "Concord, CA"
        )
    )

    private val fakeCurrentLocation = Location(
        name = "Fairfield",
        state = "CA",
        lat = 38.2493581,
        lon = -122.039966,
        formattedLocation = "Fairfield, CA"
    )

    private val fakeWeatherDetailsResponse = WeatherDetails(
        location = "Fairfield",
        windSpeed = "123mph",
        temperature = "35",
        image = "www.fakeurl.com/img",
        description = "Clear Skies"
    )

    @get:Rule
    val mockkRule = MockKRule(this)

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    lateinit var searchLocationUseCaseMock: SearchLocationUseCase

    @MockK
    lateinit var recentSearchesUseCaseMock: RecentSearchesUseCase

    @MockK
    lateinit var getWeatherUseCaseMock: GetWeatherUseCase


    private lateinit var weatherViewModel: WeatherViewModel


    companion object{
        const val SUCCESS_QUERY = "Los Angeles"
        const val ERROR_QUERY = "Detroit"
    }
    @Before
    fun setUp() {
        coEvery { searchLocationUseCaseMock.getRecentSearches() } returns ResultWrapper.Success(
            fakeRecentSearchResults
        )
        coEvery { searchLocationUseCaseMock.searchLocation(query = SUCCESS_QUERY) } returns ResultWrapper.Success(
            fakeGeoLocationSearchResults
        )
        coEvery { getWeatherUseCaseMock.getWeather(fakeCurrentLocation) } returns ResultWrapper.Success(
            fakeWeatherDetailsResponse
        )
        coEvery { getWeatherUseCaseMock.getWeatherAtCurrentLocation() } returns ResultWrapper.Success(
            fakeWeatherDetailsResponse
        )
        coEvery { recentSearchesUseCaseMock.clearAll() } returns Unit

        coEvery { searchLocationUseCaseMock.searchLocation(query = ERROR_QUERY) } returns ResultWrapper.GenericError()


        weatherViewModel = WeatherViewModel(
            searchLocationUseCase = searchLocationUseCaseMock,
            recentSearchesUseCase = recentSearchesUseCaseMock,
            getWeatherUseCase = getWeatherUseCaseMock
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test recent search call is not made on launch`() = runTest {
        //verify no calls happened
        coVerify(exactly = 0) { searchLocationUseCaseMock.getRecentSearches() }
        coVerify(exactly = 0) { recentSearchesUseCaseMock.getRecentSearches() }
        coVerify(exactly = 0) { getWeatherUseCaseMock.getWeather(fakeCurrentLocation) }
        coVerify(exactly = 0) { getWeatherUseCaseMock.getWeatherAtLastLocation() }
        coVerify(exactly = 0) { getWeatherUseCaseMock.getWeatherAtCurrentLocation() }
        //assertions
        Assert.assertNotNull(weatherViewModel.state)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test recent search call`() = runTest {
        //verify called once
        weatherViewModel.loadRecentSearches()
        coVerify(exactly = 1) { searchLocationUseCaseMock.getRecentSearches() }

        //assertions
        Assert.assertNotNull(weatherViewModel.state)
        Assert.assertEquals(
            weatherViewModel.state.value,
            WeatherFragmentState.RecentSearches(fakeRecentSearchResults)
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test search geo location by search query`() = runTest {
        weatherViewModel.searchUsingGeocode(query = SUCCESS_QUERY)
        coVerify(exactly = 1) { searchLocationUseCaseMock.searchLocation(query = SUCCESS_QUERY) }

        //assertions
        Assert.assertNotNull(weatherViewModel.state)
        Assert.assertEquals(
            weatherViewModel.state.value,
            WeatherFragmentState.Success(networkResults = fakeGeoLocationSearchResults)
        )
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `current weather updates states correctly`() = runTest {
        val resultFlow = weatherViewModel.getCurrentWeather(fakeCurrentLocation)
        coVerify(exactly = 1) { getWeatherUseCaseMock.getWeather(fakeCurrentLocation) }
        Assert.assertNotNull(resultFlow)
        Assert.assertTrue(weatherViewModel.state.value is WeatherFragmentState.CurrentWeather)
        Assert.assertNotNull((weatherViewModel.state.value as WeatherFragmentState.CurrentWeather).response)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `weather at current location passes`() = runTest {
        val resultFlow = weatherViewModel.getWeatherAtCurrentLocation()
        coVerify(exactly = 1) { getWeatherUseCaseMock.getWeatherAtCurrentLocation() }
        Assert.assertNotNull(resultFlow)
        Assert.assertTrue(weatherViewModel.state.value is WeatherFragmentState.CurrentWeather)
        Assert.assertNotNull((weatherViewModel.state.value as WeatherFragmentState.CurrentWeather).response)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `clear history clears results`() = runTest {
        val resultFlow = weatherViewModel.clearHistory()
        coVerify(exactly = 1) { recentSearchesUseCaseMock.clearAll() }
        Assert.assertNotNull(resultFlow)
        Assert.assertTrue(weatherViewModel.state.value is WeatherFragmentState.RecentSearches)
        Assert.assertNotNull((weatherViewModel.state.value as WeatherFragmentState.RecentSearches).cachedResults)
        Assert.assertEquals(
            (weatherViewModel.state.value as WeatherFragmentState.RecentSearches).cachedResults,
            emptyList<Location>()
        )
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `verify search location returns errors`() = runTest {
        val resultFlow = weatherViewModel.searchUsingGeocode(query = ERROR_QUERY)
        coVerify(exactly = 1) { searchLocationUseCaseMock.searchLocation(query = ERROR_QUERY) }
        Assert.assertNotNull(resultFlow)
        Assert.assertTrue(weatherViewModel.state.value is  WeatherFragmentState.Error)
    }
}