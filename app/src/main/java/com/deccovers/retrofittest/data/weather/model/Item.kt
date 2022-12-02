package com.deccovers.retrofittest.data.weather.model

data class Item(
    val forecasts: List<Forecast>,
    val timestamp: String,
    val update_timestamp: String,
    val valid_period: ValidPeriod
)