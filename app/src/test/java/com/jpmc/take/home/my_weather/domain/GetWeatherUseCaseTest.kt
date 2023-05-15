package com.jpmc.take.home.my_weather.domain


import com.jpmc.take.home.my_weather.core.ResultWrapper
import com.jpmc.take.home.my_weather.data.WeatherRepository
import com.jpmc.take.home.my_weather.data.dto.Location
import com.jpmc.take.home.my_weather.data.weather.Weather
import com.jpmc.take.home.my_weather.data.weather.WeatherResponse
import com.jpmc.take.home.my_weather.domain.location.GetCurrentLocationUseCase
import com.jpmc.take.home.my_weather.domain.recent_searches.RecentSearchesUseCase
import com.jpmc.take.home.my_weather.domain.weather.GetWeatherUseCase
import com.jpmc.take.home.my_weather.domain.weather.impl.GetWeatherUseCaseImpl
import com.jpmc.take.home.my_weather.presentation.weather.WeatherFragmentState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.unmockkObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetWeatherUseCaseTest {
    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var weatherRepositoryMock: WeatherRepository

    @MockK
    lateinit var recentSearchesUseCaseMock: RecentSearchesUseCase

    @MockK
    lateinit var getCurrentLocationUseCaseMock: GetCurrentLocationUseCase

    private lateinit var getWeatherUseCase: GetWeatherUseCase

    private val fakeLocation =
        Location(
            lat = 0.0,
            lon = 0.0,
            name = "San Francisco",
            state = "CA",
            formattedLocation = "San Francisco, CA"
        )


    private val fakeRecentSearchResults = listOf(
        Location(
            lat = 0.0,
            lon = 0.0,
            name = "Concord",
            state = "CA",
            formattedLocation = "Concord, CA"
        )
    )

    private val fakeWeatherResponse = WeatherResponse(
        null, null, null, null, null, null, null, "fake name", null, null, null,
        listOf(
            Weather(
                description = "clear sky", null, null, null
            )
        ), null
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        coEvery { weatherRepositoryMock.getWeather(any()) } returns ResultWrapper.Success(
            fakeWeatherResponse
        )
        coEvery { recentSearchesUseCaseMock.getRecentSearches() } returns ResultWrapper.Success(
            fakeRecentSearchResults
        )
        coEvery { recentSearchesUseCaseMock.addRecentSearch(fakeLocation) } returns Unit

        coEvery { getCurrentLocationUseCaseMock.getCurrentLocation() } returns Pair(
            first = 38.2493581,
            second = -122.039966
        )

        getWeatherUseCase = GetWeatherUseCaseImpl(
            weatherRepositoryMock,
            recentSearchesUseCaseMock,
            getCurrentLocationUseCaseMock
        )

    }


    @After
    fun afterTests() {
        unmockkObject(weatherRepositoryMock)
        unmockkObject(recentSearchesUseCaseMock)
        unmockkObject(getCurrentLocationUseCaseMock)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `get weather returns expected results`() = runTest {
        val result = getWeatherUseCase.getWeather(fakeLocation)
        coVerify(exactly = 1) { getWeatherUseCase.getWeather(fakeLocation) }
        Assert.assertNotNull(result)
        Assert.assertTrue(result is ResultWrapper.Success)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `get weather at current location should return expected results`() = runTest {
        val result = getWeatherUseCase.getWeatherAtCurrentLocation()
        coVerify(exactly = 1) { getCurrentLocationUseCaseMock.getCurrentLocation() }
        coVerify(exactly = 1) { weatherRepositoryMock.getWeather(any()) }

        Assert.assertNotNull(result)
        Assert.assertTrue(result is ResultWrapper.Success)
    }

}