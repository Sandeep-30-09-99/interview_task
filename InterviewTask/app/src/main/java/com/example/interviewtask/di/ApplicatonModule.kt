package com.example.interviewtask.di

import com.example.interviewtask.MyApp
import com.example.interviewtask.network.ApiHelper
import com.example.interviewtask.network.ApiHelperImp
import com.example.interviewtask.network.ApiService
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ApplicatonModule {


    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder().build()
    }

    @Provides
    fun getRetrofit(): Retrofit {
        val gson = GsonBuilder().create()
        return Retrofit.Builder().baseUrl("https://fileupload.vinnisoft.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(provideOkHttpClient())
            .build()

    }

    @Provides
    fun ApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    fun provideApiHelper2(apiHelper: ApiHelperImp): ApiHelper = apiHelper


}