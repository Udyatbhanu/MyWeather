package com.jpmc.take.home.my_weather.domain

import com.jpmc.take.home.my_weather.AppManager
import com.jpmc.take.home.my_weather.core.CurrentLocation
import com.jpmc.take.home.my_weather.domain.location.GetCurrentLocationUseCase
import com.jpmc.take.home.my_weather.domain.location.impl.GetCurrentLocationUseCaseImpl
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetLastLocationUseCaseTest {
    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var appManagerMock : AppManager

    private lateinit var getLastLocationUseCase: GetCurrentLocationUseCase
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getLastLocationUseCase = GetCurrentLocationUseCaseImpl(appManagerMock)
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `get location is returned correctly`() = runTest{
        coEvery { appManagerMock.location } returns CurrentLocation( latitude = 38.2493581, longitude = -122.039966)
        val result = getLastLocationUseCase.getCurrentLocation()
        coVerify (exactly = 1){  appManagerMock.location }
        Assert.assertNotNull(result)
        Assert.assertEquals(result.first, 38.2493581, 0.0)
        Assert.assertEquals(result.second, -122.039966, 0.0)
    }
}