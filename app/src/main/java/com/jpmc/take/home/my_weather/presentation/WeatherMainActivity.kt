package com.jpmc.take.home.my_weather.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.jpmc.take.home.my_weather.R
import com.jpmc.take.home.my_weather.core.SessionViewModel
import com.jpmc.take.home.my_weather.core.WeatherBaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


sealed class WeatherActions {
    object AttemptCurrentLocation : WeatherActions()

    object Idle : WeatherActions()
}

@AndroidEntryPoint
class WeatherMainActivity : WeatherBaseActivity() {
    private val sessionViewModel by viewModels<SessionViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Set up an OnPreDrawListener to the root view, this is left blank on purpose for future app initialization work in the app manager.
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    // Check whether the initial data is ready.
                    return if (true) {
                        // The content is ready. Start drawing.
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        // The content isn't ready. Suspend.
                        false
                    }
                }
            }
        )

    }

    override fun onResume() {
        super.onResume()
        subscribe()
    }


    private fun subscribe() {
        lifecycleScope.launch {
            sessionViewModel.action.collect { action ->
                when (action) {
                    is WeatherActions.AttemptCurrentLocation -> attemptLocation()
                    else -> {} //do nothing.
                }
            }
        }

    }
}