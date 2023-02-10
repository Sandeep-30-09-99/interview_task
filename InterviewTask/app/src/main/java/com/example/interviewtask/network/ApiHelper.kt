package com.example.interviewtask.network

import com.google.gson.JsonObject
import retrofit2.Call


interface ApiHelper {

    fun getData(): retrofit2.Call<JsonObject>

}