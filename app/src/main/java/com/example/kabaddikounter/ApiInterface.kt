package com.example.kabaddikounter

import com.example.kabaddikounter.data.entities.Score
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {
    @GET("match")
    suspend fun getMatches(): Response<List<Score>>
}