package com.example.interviewtask.network

import com.example.interviewtask.model.NewsApiResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response


interface ApiHelper {
   suspend fun getHeadlines(): Response<NewsApiResponse>
}