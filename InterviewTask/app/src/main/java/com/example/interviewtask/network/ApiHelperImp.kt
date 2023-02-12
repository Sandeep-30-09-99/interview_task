package com.example.interviewtask.network


import com.example.interviewtask.model.NewsApiResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImp @Inject constructor(val apiService: ApiService) : ApiHelper {


    override suspend fun getHeadlines(): Response<NewsApiResponse> {
        return apiService.getHeadLines("3835e8c4689a49c09b5a0b8848ad343e", "en")
    }
}