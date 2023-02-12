package com.example.interviewtask.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

data class NewsApiResponse(
    val articles: List<Article>, val status: String, val totalResults: Int
)

@Parcelize
data class Source(
    val id: String?, val name: String?
) : Parcelable

@Parcelize
@Entity("Article")
data class Article(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String?,
    var savedPath: String?,
    val url: String?,
    val urlToImage: String?
) : Parcelable