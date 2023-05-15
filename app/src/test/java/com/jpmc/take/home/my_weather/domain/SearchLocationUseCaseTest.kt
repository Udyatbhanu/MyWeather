package com.jpmc.take.home.my_weather.domain

import com.jpmc.take.home.my_weather.core.ResultWrapper
import com.jpmc.take.home.my_weather.data.GeocodeRepository
import com.jpmc.take.home.my_weather.data.RecentSearchesRepository
import com.jpmc.take.home.my_weather.data.database.RecentSearchEntity
import com.jpmc.take.home.my_weather.data.dto.Location
import com.jpmc.take.home.my_weather.data.geocode.SearchResultsResponseItem
import com.jpmc.take.home.my_weather.domain.geocode.SearchLocationUseCase
import com.jpmc.take.home.my_weather.domain.geocode.impl.SearchLocationUseCaseImpl
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

class SearchLocationUseCaseTest {
    @get:Rule
    val mockkRule = MockKRule(this)


    @MockK
    lateinit var geocodeRepositoryMock: GeocodeRepository

    @MockK
    lateinit var recentSearchesRepositoryMock: RecentSearchesRepository

    private lateinit var searchLocationUseCase: SearchLocationUseCase

    private val fakeRecentSearchResults = listOf(
        Location(
            lat = 0.0,
            lon = 0.0,
            name = "Concord",
            state = "CA",
            formattedLocation = "Concord, CA"
        )
    )
    private val fakeRecentSearchesRepositoryResults = listOf(
        RecentSearchEntity(
            id = "fakeId",
            0.0,
            0.0,
            location = "Concord",
            state = "CA"
        )
    )


    private val fakeGeoCodeSearchResults = listOf(
        Location(
            lat = 0.0,
            lon = 0.0,
            name = "San Francisco",
            state = "CA",
            formattedLocation = "San Francisco, CA"
        )
    )




    private val fakeGeocodeRepositoryResults = listOf(
        SearchResultsResponseItem(
            lat = 0.0,
            lon = 0.0,
            name = "San Francisco",
            state = "CA",
            country = "USA"
        )
    )


    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        coEvery { recentSearchesRepositoryMock.getRecentSearches() } returns ResultWrapper.Success(fakeRecentSearchesRepositoryResults)

        coEvery { geocodeRepositoryMock.searchLocation("SF") } coAnswers {
            ResultWrapper.Success(
                fakeGeocodeRepositoryResults
            )
        }
        searchLocationUseCase = SearchLocationUseCaseImpl(
            geocodeRepository = geocodeRepositoryMock,
            recentSearchesRepository = recentSearchesRepositoryMock
        )
    }

    @After
    fun afterTests() {
        unmockkObject(geocodeRepositoryMock)
        unmockkObject(recentSearchesRepositoryMock)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `search location with query should return results`() = runTest {

        val results = searchLocationUseCase.searchLocation("SF")
        val resultsAsSuccess = results as ResultWrapper.Success

        // verify calls happened
        coVerify (exactly = 1){ searchLocationUseCase.searchLocation("SF") }
        coVerify (exactly = 1){ geocodeRepositoryMock.searchLocation("SF") }


        //assertions
        Assert.assertNotNull(results)
        Assert.assertEquals(1, resultsAsSuccess.value.size)
        Assert.assertEquals("San Francisco, CA", resultsAsSuccess.value.first().formattedLocation)
        Assert.assertEquals(results, ResultWrapper.Success(fakeGeoCodeSearchResults))
    }




    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `recent searches should return results`() = runTest {

        val recentSearches = searchLocationUseCase.getRecentSearches()
        val recentSearchesAsSuccess =
            recentSearches as ResultWrapper.Success

        // verify calls happened
        coVerify (exactly = 1){ searchLocationUseCase.getRecentSearches() }

        //assertions
        Assert.assertNotNull(recentSearches)
        Assert.assertEquals(1, recentSearchesAsSuccess.value.size)
        Assert.assertEquals(recentSearches, ResultWrapper.Success(fakeRecentSearchResults))
    }
}