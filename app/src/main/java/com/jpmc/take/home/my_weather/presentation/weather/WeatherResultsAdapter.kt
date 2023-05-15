package com.jpmc.take.home.my_weather.presentation.weather

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.jpmc.take.home.my_weather.R
import com.jpmc.take.home.my_weather.data.dto.Location
import com.jpmc.take.home.my_weather.databinding.SearchLocationResultItemBinding

class WeatherResultsAdapter(private val listener : (Location) -> Unit) :
    ListAdapter<Location, WeatherResultsViewHolder>(COMPARATOR) {

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Location>() {
            override fun areItemsTheSame(
                oldItem: Location,
                newItem: Location
            ): Boolean = oldItem.formattedLocation == newItem.formattedLocation


            override fun areContentsTheSame(
                oldItem: Location,
                newItem: Location
            ): Boolean =
                oldItem.formattedLocation == newItem.formattedLocation
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherResultsViewHolder {
        val binding: SearchLocationResultItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.search_location_result_item,
            parent, false
        )
        return WeatherResultsViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: WeatherResultsViewHolder, position: Int) {
        val location = getItem(position)
        if (location != null) {
            holder.bind(location, listener)
        }
    }
}