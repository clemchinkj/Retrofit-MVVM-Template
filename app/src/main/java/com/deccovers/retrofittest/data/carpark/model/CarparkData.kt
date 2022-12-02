package com.deccovers.retrofittest.data.carpark.model

data class CarparkData(
    val carpark_info: List<CarparkInfo>,
    val carpark_number: String,
    val update_datetime: String
)