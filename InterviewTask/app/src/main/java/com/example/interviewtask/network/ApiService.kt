package com.example.interviewtask.network


import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("api/get-files")
    fun getData():Call<JsonObject>


}