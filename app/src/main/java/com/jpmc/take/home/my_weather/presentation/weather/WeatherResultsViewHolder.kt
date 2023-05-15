package com.jpmc.take.home.my_weather.presentation.weather

import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jpmc.take.home.my_weather.R
import com.jpmc.take.home.my_weather.data.dto.Location
import com.jpmc.take.home.my_weather.databinding.SearchLocationResultItemBinding

class WeatherResultsViewHolder(private val binding: SearchLocationResultItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(location: Location, listener : (Location) -> Unit) {
        with(binding) {
            countryName.text = location.formattedLocation

            Glide.with(root.context)
                .load(R.drawable.baseline_search_24)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(icon)
            searchResult.setOnClickListener {
                listener(location)
            }
        }
    }
}