package com.jpmc.take.home.my_weather.core.di

import com.jpmc.take.home.my_weather.core.network.Endpoints
import com.jpmc.take.home.my_weather.core.network.GeocodeApi
import com.jpmc.take.home.my_weather.core.network.WeatherApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Singleton components to provide the network modules.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    fun provideRetrofit(okHttp: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttp)
            .baseUrl(Endpoints.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    /**
     * Provide the geocode api
     */
    @Provides
    fun provideGeoApi(retrofit: Retrofit): GeocodeApi {
        return retrofit.create(GeocodeApi::class.java)
    }


    /**
     * Provide the weather api
     */
    @Provides
    fun provideWeatherApi(retrofit: Retrofit): WeatherApi {
        return retrofit.create(WeatherApi::class.java)
    }
}