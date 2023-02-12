package com.example.interviewtask.ui.view_photo


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.print.PdfPrint
import android.print.PrintAttributes
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.webkit.*
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import com.example.interviewtask.databinding.ActivityViewPhotoBinding
import com.example.interviewtask.local_storage.ProductDao
import com.example.interviewtask.local_storage.ProductDatabase
import com.example.interviewtask.model.Article
import com.example.interviewtask.util.Constant
import com.example.interviewtask.util.Coroutine
import com.example.interviewtask.util.showErrorToast
import com.example.interviewtask.util.showInfoToast
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


@AndroidEntryPoint
class FullArticleActivity : AppCompatActivity() {

    companion object {
        fun intent(
            activity: Activity, photo: Article? = null, loadFromFile: Boolean = false
        ): Intent {
            val intent = Intent(activity, FullArticleActivity::class.java)
            intent.putExtra(Constant.ARTICLE, photo)
            intent.putExtra(Constant.LOAD_FROM_FILE, loadFromFile)
            return intent
        }
    }


    private var loadFromFile: Boolean = false
    lateinit var productDao: ProductDao
    private fun initDatabase() {
        productDao = ProductDatabase.getInstance(this).noteDao()
    }

    private var article: Article? = null
    private lateinit var binding: ActivityViewPhotoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.webView.settings.domStorageEnabled
        binding.header.threeDot.visibility = View.VISIBLE
        binding.header.title.text = "Article"
        getIntentExtras()
        initDatabase()
        setClickListener()
        if (Build.VERSION.SDK_INT >= 30) {
            if (!Environment.isExternalStorageManager()) {
                val getpermission = Intent()
                getpermission.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                startActivity(getpermission)
            }
        }
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                binding.webView.visibility = View.VISIBLE
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                binding.progress.visibility = View.GONE
            }
        }
        binding.webView.setInitialScale(1)
        binding.webView.settings.allowContentAccess = true
        binding.webView.settings.allowFileAccess = true
        binding.webView.settings.allowFileAccessFromFileURLs = true
        binding.webView.settings.allowUniversalAccessFromFileURLs = true
        binding.webView.settings.loadWithOverviewMode = true
        binding.webView.settings.useWideViewPort = true
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.setSupportZoom(true)
        binding.webView.settings.builtInZoomControls = true
        binding.webView.settings.displayZoomControls = false
        if (loadFromFile) loadArchive()
    }

    private fun setClickListener() {
        binding.header.threeDot.setOnClickListener {
            showOption(it)
        }
    }

    private fun getIntentExtras() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra(Constant.ARTICLE, Article::class.java)?.let {
                article = it
            }
        } else {
            intent?.getParcelableExtra<Article>(Constant.ARTICLE)?.let {
                article = it
            }
        }
        intent?.getBooleanExtra(Constant.LOAD_FROM_FILE, false)?.let {
            loadFromFile = it
        }
        article?.url?.let {
            if (!loadFromFile) binding.webView.loadUrl(it)
        }

    }


    private fun saveWebPage() {
        val attributes = PrintAttributes.Builder().setMediaSize(PrintAttributes.MediaSize.ISO_A4)
            .setResolution(PrintAttributes.Resolution("pdf", "pdf", 600, 600))
            .setMinMargins(PrintAttributes.Margins.NO_MARGINS).build()
        val path =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/PDFTest/")
        val pdfPrint = PdfPrint(attributes)
        pdfPrint.print(binding.webView.createPrintDocumentAdapter("task_document"),
            path,
            "output_1" + ".pdf",
            object : PdfPrint.CallbackPrint {
                override fun success(path: String) {
                    showInfoToast("Saved")
                    article?.savedPath = path
                    article?.let {
                        saveArticle(it)
                    }
                }

                override fun onFailure() {
                    showErrorToast("failed")
                }
            })

    }

    private fun saveArticle(article: Article) {
        Coroutine.IO {
            productDao.insert(article)
        }
    }

    private fun showOption(view: View?) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menu.add("Save")
        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            saveWebPage()
            true
        }
        popupMenu.show()
    }

    private fun loadArchive() {
        binding.webView.visibility = View.GONE
/*
        val f =
            File("" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/PDFTest/") + "/output_1" + ".pdf")
*/
        article?.savedPath?.let {
            val f = File(it)
            Log.i("fileser", f.path.toString())
            binding.newPdf.fromFile(f).password(null).defaultPage(0).onPageError { page, _ ->
                showErrorToast("error")
            }.load()
        }

    }

}