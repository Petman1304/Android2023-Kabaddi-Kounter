package com.example.kabaddikounter

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInst {
    private const val BASE_URL = "https://kabiddi-kounter-api.petrus-nm.web.id/"

    fun getInstance(): Retrofit{
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()    }
}