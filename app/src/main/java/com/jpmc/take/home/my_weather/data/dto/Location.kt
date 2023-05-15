package com.jpmc.take.home.my_weather.data.dto

import android.os.Parcelable
import com.jpmc.take.home.my_weather.data.database.RecentSearchEntity
import com.jpmc.take.home.my_weather.data.weather.WeatherRequest
import kotlinx.parcelize.Parcelize

typealias Locations = List<Location>

@Parcelize
data class Location(
    val name: String,
    val state: String,
    val lat: Double,
    val lon: Double,
    val formattedLocation: String
) : Parcelable


fun Location.toLocationEntity(): RecentSearchEntity {
    return RecentSearchEntity(
        location = this.name,
        lat = this.lat,
        lon = this.lon,
        state = this.state,
        formattedLocation = name.plus(", ").plus(state),
    )
}

fun Location.toWeatherRequest(): WeatherRequest {
    return WeatherRequest(
        lat = this.lat,
        lon = this.lon,
    )
}

fun Pair<Double,Double>.toWeatherRequest(): WeatherRequest {
    return WeatherRequest(
        lat = this.first,
        lon = this.second,
    )
}