package com.jpmc.take.home.my_weather.data.geocode


import android.os.Parcelable
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import com.jpmc.take.home.my_weather.data.database.RecentSearchEntity
import com.jpmc.take.home.my_weather.data.dto.Location
import com.jpmc.take.home.my_weather.data.weather.WeatherRequest
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchResultsResponseItem(
    @SerializedName("country")
    val country: String?,
    @SerializedName("lat")
    val lat: Double? = 37.7790262,
    @SerializedName("lon")
    val lon: Double? = -122.419906,
    @SerializedName("name")
    val name: String?,
    @SerializedName("state")
    val state: String?
) : Parcelable



fun SearchResultsResponseItem.toLocationDTO(): Location {
    return if (this.lat == null || this.lon == null || this.name == null || this.state == null) {
         Location(
            name = "San Francisco",
            state = "CA",
            formattedLocation = "San Francisco, CA",
            lat = 37.7790262,
            lon = -122.419906
        )
    } else {
        Location(
            name = this.name,
            state = this.state,
            formattedLocation = this.name.plus(", ").plus(this.state),
            lat = this.lat,
            lon = this.lon
        )
    }

}