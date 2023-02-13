package com.example.interviewtask.local_storage

import androidx.lifecycle.LiveData
import com.example.interviewtask.model.Article
import javax.inject.Inject

class DatabaseRepositry
@Inject constructor(
    private val dao: ArticleDao
) {

    suspend fun saveProduct(note: Article) = dao.insert(note)
    suspend fun updateArticle(note: Article) = dao.update(note)
    suspend fun deleteArticle(note: Article) = dao.delete(note)
    suspend fun deleteArticleById(id: Int) = dao.deleteById(id)
    fun getArticle(): LiveData<List<Article>> = dao.getArticleList()

}