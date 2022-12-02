package com.deccovers.retrofittest.ui.weather

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deccovers.retrofittest.data.ContentRepository
import com.deccovers.retrofittest.data.carpark.model.CarparkResponse
import com.deccovers.retrofittest.data.weather.model.WeatherResponse
import com.deccovers.retrofittest.ui.carpark.CarparkViewModel
import com.deccovers.retrofittest.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val contentRepository: ContentRepository
): ViewModel() {
    sealed class WeatherEvent {
        class Success(val weatherResponse: WeatherResponse): WeatherEvent()
        class Failure(val errorText: String): WeatherEvent()
        object Loading : WeatherEvent()
        object Empty : WeatherEvent()
    }

    private val _weatherRetrieval = MutableStateFlow<WeatherEvent>(WeatherEvent.Empty)
    val weatherRetrieval: StateFlow<WeatherEvent> = _weatherRetrieval

    fun getWeatherResponse() {
        viewModelScope.launch(Dispatchers.IO) {
            _weatherRetrieval.value = WeatherViewModel.WeatherEvent.Loading

            when(val weatherResponse = contentRepository.getWeatherResponse()) {
                is Resource.Failure -> {
                    Log.e("getWeatherResponse()", "getWeatherResponse: Resource.Failure: ${weatherResponse.message}")
                    _weatherRetrieval.value = WeatherEvent.Failure(weatherResponse.message!!)
                }

                is Resource.Success -> {
                    Log.d("getWeatherResponse()", "getWeatherResponse: Resource.Success")
                    _weatherRetrieval.value = WeatherEvent.Success(weatherResponse.data!!)
                }
            }
        }
    }
}