package com.jpmc.take.home.my_weather.domain.location

interface GetCurrentLocationUseCase {
    suspend fun getCurrentLocation() : Pair<Double, Double>
}