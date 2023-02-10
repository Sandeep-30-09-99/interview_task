package com.example.interviewtask.network


import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImp @Inject constructor(val apiService: ApiService) : ApiHelper {

    override fun getData():Call<JsonObject> {
        return apiService.getData()
    }
}