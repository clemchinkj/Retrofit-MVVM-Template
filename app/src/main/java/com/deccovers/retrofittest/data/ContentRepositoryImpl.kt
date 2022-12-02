package com.deccovers.retrofittest.data

import android.util.Log
import com.deccovers.retrofittest.data.carpark.CarparkApi
import com.deccovers.retrofittest.data.carpark.model.CarparkResponse
import com.deccovers.retrofittest.data.weather.WeatherApi
import com.deccovers.retrofittest.data.weather.model.WeatherResponse
import com.deccovers.retrofittest.util.Resource
import javax.inject.Inject

private const val TAG = "ContentRepositoryImpl"

class ContentRepositoryImpl @Inject constructor(
    private val carparkApi: CarparkApi,
    private val weatherApi: WeatherApi
): ContentRepository {
    override suspend fun getCarparkResponse(): Resource<CarparkResponse> {
        return try {
            val response = carparkApi.getCarparks()
            val result = response.body()

            Log.d(TAG, "getCarparkResponse: response = $response, result = $result")
            if (response.isSuccessful && result != null) {
                Log.d(TAG, "getCarparkResponse: $result")
                Resource.Success(result)
            } else {
                Resource.Failure(response.message())
            }
        } catch (e: Exception) {
            Resource.Failure(e.message ?: "An error occurred")
        }
    }

    override suspend fun getWeatherResponse(): Resource<WeatherResponse> {
        return try {
            val response = weatherApi.getWeather()
            val result = response.body()

            Log.d(TAG, "getWeatherResponse: response = $response, result = $result")
            if (response.isSuccessful && result != null) {
                Log.d(TAG, "getWeatherResponse: $result")
                Resource.Success(result)
            } else {
                Resource.Failure(response.message())
            }
        } catch (e: Exception) {
            Resource.Failure(e.message ?: "An error occured")
        }
    }
}