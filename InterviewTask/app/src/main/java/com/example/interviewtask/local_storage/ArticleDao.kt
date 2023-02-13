package com.example.interviewtask.local_storage

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.interviewtask.model.Article

@Dao
interface ArticleDao {

    @Query("SELECT * FROM Article")
    fun getArticleList(): LiveData<List<Article>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(article: Article)


    @Query("DELETE FROM Article WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Delete
    suspend fun delete(article: Article)

    @Update
    suspend fun update(article: Article)
}

