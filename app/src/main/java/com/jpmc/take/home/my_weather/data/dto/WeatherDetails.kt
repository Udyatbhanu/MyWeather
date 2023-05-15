package com.jpmc.take.home.my_weather.data.dto

data class WeatherDetails(
    val location: String,
    val windSpeed: String,
    val temperature: String,
    val image: String,
    val description: String
)