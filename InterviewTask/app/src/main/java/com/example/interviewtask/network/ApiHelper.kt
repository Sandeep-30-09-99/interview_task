package com.example.interviewtask.network

import com.example.interviewtask.model.ApiData
import com.example.interviewtask.model.Data
import com.google.gson.JsonObject
import retrofit2.Call


interface ApiHelper {

    fun getData(): Call<JsonObject>

}