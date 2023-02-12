package com.example.interviewtask.util

data class Resource<T>(val status: Status,val msg: String? = null, val data: T? = null) {
    companion object {
        fun <T> loading(): Resource<T> {
            return Resource(Status.LOADING)
        }

        fun <T> success(msg: String, data: T): Resource<T> {
            return Resource(Status.SUCCESS, msg, data)
        }

        fun <T> error(msg: String): Resource<T> {
            return Resource(Status.ERROR, msg)
        }
    }
}

enum class Status {
    LOADING, SUCCESS, ERROR
}