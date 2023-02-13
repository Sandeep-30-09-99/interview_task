package com.example.interviewtask.ui.article


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.interviewtask.R
import com.example.interviewtask.databinding.ActivityShowProductBinding
import com.example.interviewtask.local_storage.ArticleDao
import com.example.interviewtask.local_storage.ArticleDatabase
import com.example.interviewtask.model.Article
import com.example.interviewtask.model.Options
import com.example.interviewtask.ui.AdapterCallback
import com.example.interviewtask.ui.ArticleAdapter
import com.example.interviewtask.ui.view_photo.FullArticleActivity
import com.example.interviewtask.util.*
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ArticleActivity : AppCompatActivity() {

    companion object {
        var localDataLoaded = false
        fun intent(activity: Activity): Intent {
            val intent = Intent(activity, ArticleActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            return intent
        }
    }


    lateinit var productDao: ArticleDao
    private fun initDatabase() {
        productDao = ArticleDatabase.getInstance(this).noteDao()
    }

    private val viewModel: ArticleVM by viewModels()

    private lateinit var binding: ActivityShowProductBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //  Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler(this))
        binding = ActivityShowProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.vm = viewModel
        binding.header.title.text =
            if (isNetworkAvailable()) getString(R.string.top_headlines) else getString(R.string.saved_post)
        binding.header.threeDot.visibility = View.VISIBLE
        initDatabase()
        initRV()
        listenClicks()
        setObserver()
        if (isNetworkAvailable()) getLatestHeadlines()
        else loadSavedArticle()

    }

    private fun getLatestHeadlines() {
        viewModel.getTopHeadLines()
        localDataLoaded = false
    }

    private fun loadSavedArticle() {
        binding.progress.visibility = View.VISIBLE
        productDao.getArticleList().observe(this, Observer {
            if (it.isNotEmpty()) {
                binding.header.title.text = getString(R.string.saved_post)
                localDataLoaded = true

            } else {
                showToast("No Saved Post")
                if (isNetworkAvailable()) {
                    binding.header.title.text = getString(R.string.top_headlines)
                    getLatestHeadlines()
                    localDataLoaded = false
                } else {
                    binding.header.title.text = getString(R.string.saved_post)
                    localDataLoaded = true
                }
            }
            setListInAdapter(it as ArrayList<Article>)
            binding.progress.visibility = View.GONE
        })
    }


    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }


    private var articleAdapter: ArticleAdapter? = null
    private fun initRV() {
        articleAdapter = ArticleAdapter(this, object : AdapterCallback {
            override fun onViewClick(v: View, pos: Int, bean: Article) {
                when (v.id) {
                    R.id.ivDelete -> {
                        articleAdapter?.deletePos(pos)
                        deleteArticleFromDatabase(bean)
                    }
                    R.id.iv -> {
                        startActivity(
                            FullArticleActivity.intent(
                                this@ArticleActivity, bean, localDataLoaded
                            )
                        )
                    }
                }
            }
        })
        binding.rvProduct.adapter = articleAdapter

    }

    private fun deleteArticleFromDatabase(article: Article) {
        Coroutine.IO {
            productDao.delete(article)
        }
    }

    private fun setListInAdapter(list: ArrayList<Article>?) {
        if (list.isNullOrEmpty()) {
            binding.tvNoData.visibility = View.VISIBLE
            binding.rvProduct.visibility = View.GONE
        } else {
            binding.tvNoData.visibility = View.GONE
            binding.rvProduct.visibility = View.VISIBLE
        }
        list?.let {
            articleAdapter?.setList(it)
        }
    }

    private fun listenClicks() {
        binding.header.threeDot.setOnClickListener {
            showOption(
                option = if (localDataLoaded) Options.LATEST.text else Options.SAVED_ARTICLE.text,
                view = binding.header.threeDot
            )
        }
    }


    private fun showOption(option: String, view: View?) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menu.add(option)
        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.title) {
                Options.SAVED_ARTICLE.text -> {
                    loadSavedArticle()
                }
                Options.LATEST.text -> {
                    if (isNetworkAvailable()) getLatestHeadlines()
                    else showToast("No Internet")
                }
            }
            true
        }
        popupMenu.show()
    }

    private fun setObserver() {
        viewModel.articleList.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    binding.progress.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    binding.header.title.text = getString(R.string.top_headlines)
                    localDataLoaded = false
                    Log.i("response", it.data.toString())
                    setListInAdapter(it.data as ArrayList<Article>)

                }
                Status.ERROR -> {
                    binding.progress.visibility = View.GONE
                    if (isNetworkAvailable()) {
                        binding.header.title.text = getString(R.string.top_headlines)
                        localDataLoaded = false
                    } else {
                        localDataLoaded = true
                        binding.header.title.text = getString(R.string.saved_post)
                    }
                    showToast(it.msg.toString())
                }
            }
        })
    }


}