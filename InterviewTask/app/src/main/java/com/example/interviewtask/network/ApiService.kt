package com.example.interviewtask.network


import com.example.interviewtask.model.NewsApiResponse
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.HeaderMap
import retrofit2.http.Query

interface ApiService {

    @GET("v2/top-headlines")
    suspend fun getHeadLines(
        @Header("x-api-key") apiKey: String,
        @Query("q") language: String
    ): Response<NewsApiResponse>
}