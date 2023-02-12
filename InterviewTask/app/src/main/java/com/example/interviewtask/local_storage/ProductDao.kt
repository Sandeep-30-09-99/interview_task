package com.example.interviewtask.local_storage

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.interviewtask.model.Article
import com.example.interviewtask.model.Product

@Dao
interface ProductDao {

    @Query("SELECT * FROM Article")
    fun getArticleList(): LiveData<List<Article>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(product: Article)


    @Query("DELETE FROM Article WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Delete
    suspend fun delete(product: Article)

    @Update
    suspend fun update(product: Article)
}

