package com.example.interviewtask.ui.home


import android.Manifest
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.interviewtask.R
import com.example.interviewtask.databinding.ActivityHomeBinding
import com.example.interviewtask.ui.AdapterCallback
import com.example.interviewtask.ui.ListAdapter
import com.example.interviewtask.databinding.ActivityMainBinding
import com.example.interviewtask.model.Data
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {


    private val viewModel: HomeVM by viewModels()

    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //  Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler(this))
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRV()

        requestPermission()
        setObserver()
        getApiData()
    }


    override fun onDestroy() {
                super.onDestroy()
    }

    private var adapter: ListAdapter? = null
    private fun initRV() {
        adapter = ListAdapter(this, object : AdapterCallback {
            override fun onViewClick(v: View, bean: Data) {
                Log.i("abc", bean.path)
                when (v.id) {
                    R.id.ivDownload -> {

                    }
                }
            }
        })

    }

    private fun setObserver() {
        viewModel.apiData.observe(this) {
            adapter?.setList(it)

        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this@HomeActivity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 12
        )
    }

    private fun getApiData() {
        viewModel.getApiData()
    }


}