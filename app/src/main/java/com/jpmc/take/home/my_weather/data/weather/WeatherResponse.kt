package com.jpmc.take.home.my_weather.data.weather


import com.google.gson.annotations.SerializedName
import com.jpmc.take.home.my_weather.core.Constants.Companion.IMAGE_URL_PREFIX
import com.jpmc.take.home.my_weather.core.Constants.Companion.IMAGE_URL_SUFFIX
import com.jpmc.take.home.my_weather.data.dto.WeatherDetails

data class WeatherResponse(
    @SerializedName("base")
    val base: String?,
    @SerializedName("clouds")
    val clouds: Clouds?,
    @SerializedName("cod")
    val cod: Int?,
    @SerializedName("coord")
    val coord: Coord?,
    @SerializedName("dt")
    val dt: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("main")
    val main: Main?,
    @SerializedName("name")
    val name: String,
    @SerializedName("sys")
    val sys: Sys?,
    @SerializedName("timezone")
    val timezone: Int?,
    @SerializedName("visibility")
    val visibility: Int?,
    @SerializedName("weather")
    val weather: List<Weather?>?,
    @SerializedName("wind")
    val wind: Wind?
)

fun WeatherResponse.toWeatherDetails(): WeatherDetails {
    return WeatherDetails(
        location = this.name,
        temperature = main?.temp.toString().plus(" ").plus("\u2109"),
        windSpeed = wind?.speed.toString(),
        image = IMAGE_URL_PREFIX.plus(this.weather?.first()?.icon).plus(IMAGE_URL_SUFFIX) ?: "",
        description = this.weather?.first()?.description?.replaceFirstChar { it.uppercase() } ?: ""
    )
}

