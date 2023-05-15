package com.jpmc.take.home.my_weather.domain.location.impl

import com.jpmc.take.home.my_weather.AppManager
import com.jpmc.take.home.my_weather.domain.location.GetCurrentLocationUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetCurrentLocationUseCaseImpl @Inject constructor(private val appManager: AppManager) :
    GetCurrentLocationUseCase {
    override suspend fun getCurrentLocation(): Pair<Double, Double> {
        //we are reading from memory
        return withContext(Dispatchers.IO) {
            with(appManager.location) {
                 Pair(first = this?.latitude ?: 37.7790262, second = this?.longitude ?: -122.419906)
            }
        }
    }
}