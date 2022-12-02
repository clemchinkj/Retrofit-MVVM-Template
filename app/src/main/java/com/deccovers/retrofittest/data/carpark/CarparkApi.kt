package com.deccovers.retrofittest.data.carpark

import com.deccovers.retrofittest.data.carpark.model.CarparkResponse
import retrofit2.Response
import retrofit2.http.GET

interface CarparkApi {
    @GET("v1/transport/carpark-availability")
    suspend fun getCarparks(): Response<CarparkResponse>
}