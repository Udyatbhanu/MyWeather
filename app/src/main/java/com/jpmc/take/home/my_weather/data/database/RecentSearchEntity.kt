package com.jpmc.take.home.my_weather.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jpmc.take.home.my_weather.core.Constants.Companion.LAST_SEARCH_DB
import com.jpmc.take.home.my_weather.data.dto.Location
import java.util.UUID

@Entity(tableName = LAST_SEARCH_DB)
data class RecentSearchEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val lat: Double,
    val lon: Double,
    val location: String,
    val state: String,
    val formattedLocation: String = location.plus(", ").plus(state),
)

fun RecentSearchEntity.toLocationDTO(): Location {
    return Location(
        name = this.location,
        lat = this.lat,
        lon = this.lon,
        formattedLocation = this.location.plus(", ").plus(this.state),
        state = this.state
    )
}