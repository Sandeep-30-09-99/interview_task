package com.example.interviewtask.model

import com.google.gson.annotations.SerializedName


data class AData(
    val a: ArrayList<Data>
)

data class ApiData(
    @SerializedName("data")
    val list: List<Data>,
    val message: String,
    val success: Boolean
)