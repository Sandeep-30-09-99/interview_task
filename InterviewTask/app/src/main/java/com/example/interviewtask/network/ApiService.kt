package com.example.interviewtask.network


import com.example.interviewtask.model.NewsApiResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HeaderMap

interface ApiService {

    @GET("v2/top-headlines")
    fun getHeadLines(@HeaderMap header: HashMap<String, String>):  Response<NewsApiResponse>
}