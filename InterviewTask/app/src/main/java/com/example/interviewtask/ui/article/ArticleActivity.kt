package com.example.interviewtask.ui.article


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.interviewtask.R
import com.example.interviewtask.databinding.ActivityShowProductBinding
import com.example.interviewtask.local_storage.ProductDao
import com.example.interviewtask.local_storage.ProductDatabase
import com.example.interviewtask.model.Article
import com.example.interviewtask.ui.AdapterCallback
import com.example.interviewtask.ui.ProductAdapter
import com.example.interviewtask.ui.view_photo.FullArticleActivity
import com.example.interviewtask.util.Coroutine
import com.example.interviewtask.util.Status
import com.example.interviewtask.util.showErrorToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ArticleActivity : AppCompatActivity() {

    companion object {
        fun intent(activity: Activity): Intent {
            val intent = Intent(activity, ArticleActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            return intent
        }
    }


    lateinit var productDao: ProductDao
    private fun initDatabase() {
        productDao = ProductDatabase.getInstance(this).noteDao()
    }

    private val viewModel: ArticleVM by viewModels()

    private lateinit var binding: ActivityShowProductBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //  Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler(this))
        binding = ActivityShowProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.header.title.text = "Top Headlines"
        initDatabase()
        binding.vm = viewModel
        initRV()
        listenClicks()
        setObserver()
        //viewModel.getProductList
        if (isNetworkAvailable()) {
            viewModel.getTopHeadLines()
            localDataLoaded = false
        } else {
            localDataLoaded = true
            productDao.getArticleList().observe(this, Observer {
                if (it.isNotEmpty()) {
                    articleAdapter?.setList(it as ArrayList<Article>)
                }
            })
        }
    }

    private var localDataLoaded = false

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }


    private var articleAdapter: ProductAdapter? = null
    private fun initRV() {
        articleAdapter = ProductAdapter(this, object : AdapterCallback {
            override fun onViewClick(v: View, pos: Int, bean: Article) {
                when (v.id) {
                    R.id.iv -> {
                        startActivity(
                            FullArticleActivity.intent(
                                this@ArticleActivity, bean
                            )
                        )
                    }
                }
            }
        })
        binding.rvProduct.adapter = articleAdapter

    }


    private fun listenClicks() {
        /*   viewModel.onClick.observe(this, Observer {
               when (it.id) {
                   R.id.iv -> {
                   }
               }
           })*/
    }

    private fun setObserver() {
        viewModel.articleList.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    Log.i("response", it.data.toString())
                    if (it.data?.isNotEmpty() == true) {
                        articleAdapter?.setList(it.data as ArrayList<Article>)
                    }
                }
                Status.ERROR -> {
                    showErrorToast(it.msg.toString())
                }
            }
        })
    }


}