package com.duwinurmayanti.insomers.network

import com.duwinurmayanti.insomers.model.TimeResponse
import retrofit2.Call
import retrofit2.http.GET

interface TimeApi {
    @GET("/home")
    fun getCurrentTime(): Call<TimeResponse>
}