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
import android.view.MenuItem
import android.view.View
import android.webkit.*
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import com.example.interviewtask.databinding.ActivityViewPhotoBinding
import com.example.interviewtask.model.Article
import com.example.interviewtask.util.Constant
import com.example.interviewtask.util.showErrorToast
import com.example.interviewtask.util.showInfoToast
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


@AndroidEntryPoint
class FullArticleActivity : AppCompatActivity() {

    companion object {
        fun intent(activity: Activity, photo: Article? = null): Intent {
            val intent = Intent(activity, FullArticleActivity::class.java)
            intent.putExtra(Constant.ARTICLE, photo)
            return intent
        }
    }


    private var article: Article? = null
    private lateinit var binding: ActivityViewPhotoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.header.threeDot.visibility = View.VISIBLE
        binding.header.title.text = "Article"
        getIntentExtras()
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
                //  view.saveWebArchive("" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + File.separator + "myArchive" + ".pdf");
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
        loadArchive()
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
        article?.url?.let {
            //  binding.webView.loadUrl(it)
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
                    showInfoToast("saved")
                }

                override fun onFailure() {
                    showErrorToast("failed")
                }
            })

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
        /*
          binding.webView.loadUrl("file:///" + Environment.getExternalStorageDirectory() + File.separator + "myArchive" + ".pdf")
          */

        val f =
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/PDFTest/").path + "output_1" + ".pdf")
        binding.webView.loadUrl(
            Uri.fromFile(f).toString()
        )
    }

}