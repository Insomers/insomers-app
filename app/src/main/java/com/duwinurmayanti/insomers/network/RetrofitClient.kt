package com.duwinurmayanti.insomers.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://backend-dot-sylvan-replica-443303-m9.et.r.appspot.com/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val timeApi: TimeApi by lazy {
        retrofit.create(TimeApi::class.java)
    }
}
