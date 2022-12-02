package com.deccovers.retrofittest.di

import com.deccovers.retrofittest.data.ContentRepository
import com.deccovers.retrofittest.data.ContentRepositoryImpl
import com.deccovers.retrofittest.data.carpark.CarparkApi
import com.deccovers.retrofittest.data.weather.WeatherApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val SG_GOV_API_URL = "https://api.data.gov.sg/"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideCarparkApi(): CarparkApi = Retrofit.Builder()
        .baseUrl(SG_GOV_API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CarparkApi::class.java)

    @Singleton
    @Provides
    fun providesWeatherApi(): WeatherApi = Retrofit.Builder()
        .baseUrl(SG_GOV_API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WeatherApi::class.java)

    @Singleton
    @Provides
    fun provideContentRepository(carparkApi: CarparkApi, weatherApi: WeatherApi): ContentRepository = ContentRepositoryImpl(carparkApi, weatherApi)
}