package com.example.interviewtask.network


import com.example.interviewtask.model.NewsApiResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImp @Inject constructor(val apiService: ApiService) : ApiHelper {

    override fun getHeadlines():  Response<NewsApiResponse> {
        val header = HashMap<String, String>()
       //  ?q=en&apiKey=3835e8c4689a49c09b5a0b8848ad343e&page=1&limit=20
        header["q"] = "en"
        header["apiKey"] = "3835e8c4689a49c09b5a0b8848ad343e"
        return apiService.getHeadLines(header)
    }
}