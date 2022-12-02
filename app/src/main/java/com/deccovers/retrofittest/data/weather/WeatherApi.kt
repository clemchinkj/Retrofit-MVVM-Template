package com.deccovers.retrofittest.data.weather

import com.deccovers.retrofittest.data.weather.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET

interface WeatherApi {
    @GET("v1/environment/2-hour-weather-forecast")
    suspend fun getWeather(): Response<WeatherResponse>
}