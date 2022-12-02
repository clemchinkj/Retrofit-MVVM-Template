package com.deccovers.retrofittest.data

import com.deccovers.retrofittest.data.carpark.model.CarparkResponse
import com.deccovers.retrofittest.data.weather.model.WeatherResponse
import com.deccovers.retrofittest.util.Resource

interface ContentRepository {

    suspend fun getCarparkResponse(): Resource<CarparkResponse>

    suspend fun getWeatherResponse(): Resource<WeatherResponse>
}