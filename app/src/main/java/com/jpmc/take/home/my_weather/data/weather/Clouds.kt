package com.jpmc.take.home.my_weather.data.weather


import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName("all")
    val all: Int?
)