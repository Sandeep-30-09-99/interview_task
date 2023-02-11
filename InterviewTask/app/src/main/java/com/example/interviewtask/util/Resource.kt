package com.example.interviewtask.util

data class Resource<T>(val msg: String? = null, val data: T? = null) {
    companion object {
        fun <T> loading(): Resource<T> {
            return Resource()
        }

        fun <T> success(msg: String, data: T): Resource<T> {
            return Resource(msg, data)
        }

        fun <T> error(msg: String): Resource<T> {
            return Resource(msg)
        }
    }
}