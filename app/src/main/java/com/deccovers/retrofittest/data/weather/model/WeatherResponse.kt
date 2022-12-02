package com.deccovers.retrofittest.data.weather.model

data class WeatherResponse(
    val api_info: ApiInfo,
    val area_metadata: List<AreaMetadata>,
    val items: List<Item>
)