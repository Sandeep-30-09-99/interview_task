package com.example.interviewtask.ui.article


import com.example.interviewtask.model.Article

import com.example.interviewtask.network.ApiHelper

import com.example.interviewtask.ui.base.BaseViewModel
import com.example.interviewtask.util.Coroutine
import com.example.interviewtask.util.Resource
import com.example.interviewtask.util.SingleLiveEvent

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler

import javax.inject.Inject

@HiltViewModel
class ArticleVM @Inject constructor(val apiHelper: ApiHelper) : BaseViewModel() {

    val articleList = SingleLiveEvent<List<Article>>()
    fun getTopHeadLines() {
        Coroutine.IO({
            articleList.postValue(Resource.loading())
            val response = apiHelper.getHeadlines()
            if (response.isSuccessful) {
                articleList.postValue(
                    Resource.success(
                        response.message(), response.body()?.articles
                    )
                )
            } else {
                articleList.postValue(Resource.error(response.message()))
            }
        }, CoroutineExceptionHandler { _, e ->
            articleList.postValue(Resource.error(e.localizedMessage.toString()))
        })
    }
}