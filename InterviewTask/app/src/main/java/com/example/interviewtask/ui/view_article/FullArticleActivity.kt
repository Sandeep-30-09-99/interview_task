package com.example.interviewtask.ui.view_photo


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.interviewtask.R
import com.example.interviewtask.databinding.ActivityViewPhotoBinding
import com.example.interviewtask.databinding.LayoutOkBinding
import com.example.interviewtask.local_storage.ArticleDao
import com.example.interviewtask.local_storage.ArticleDatabase
import com.example.interviewtask.model.Article
import com.example.interviewtask.util.*
import com.example.interviewtask.util.dialog.BaseCustomDialog
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.ArrayList


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
    lateinit var productDao: ArticleDao
    private fun initDatabase() {
        productDao = ArticleDatabase.getInstance(this).noteDao()
    }

    private var article: Article? = null
    private lateinit var binding: ActivityViewPhotoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.webView.settings.domStorageEnabled
        binding.header.threeDot.visibility = if (loadFromFile) View.GONE else View.VISIBLE
        binding.header.title.text = if (loadFromFile) "Saved Article" else "Article "
        getIntentExtras()
        initPermissionRequiredForTaskDialog()

        initDatabase()
        setClickListener()

        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)

                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                binding.webView.visibility = View.VISIBLE
                binding.progress.visibility = View.VISIBLE
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView, url: String) {
                binding.progress.visibility = View.GONE
                super.onPageFinished(view, url)
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
            checkForPermission()
        }
    }


    private fun checkForPermission() {
        if (!hasPermissions(this, permissions)) {
            permissionResultLauncher.launch(permissions)
        } else {
            showOption(binding.header.threeDot)
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

    private val permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private var permissionDeniedDialog: BaseCustomDialog<LayoutOkBinding>? = null
    private fun initPermissionRequiredForTaskDialog() {
        permissionDeniedDialog = BaseCustomDialog(this, R.layout.layout_ok) {
            when (it.id) {
                R.id.tvOk -> {
                    permissionDeniedDialog?.cancel()
                    if (!manageFilePermissionDenied) {
                        launchForManageFilePermission()
                    } else {
                        permissionResultLauncher.launch(permissions)
                    }
                }
            }
        }
        permissionDeniedDialog?.binding?.tvTitle?.text = getString(R.string.neccessary_permission)
        permissionDeniedDialog?.setCancelable(true)
    }


    private lateinit var allGranted: ArrayList<Boolean>
    private val permissionResultLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        allGranted = ArrayList<Boolean>()// Handle Permission granted/rejected
        permissions.entries.forEach {
            it.key
            val isGranted = it.value
            allGranted.add(isGranted)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(it.key)) {
                    permissionDeniedDialog?.show()
                } else if (!it.value) {
                    showToast("Go To Setting to enable permission.")
                }
            } else {
                if (!it.value) {
                    showToast("Go To Setting to enable permission.")
                }
            }
        }
        if (!allGranted.contains(false)) {
            if (Build.VERSION.SDK_INT >= 30) {
                if (!Environment.isExternalStorageManager()) {
                    launchForManageFilePermission()
                }
            } else {
                showOption(binding.header.threeDot)
            }
        }
    }


    private var manageFilePermissionDenied = true
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (Build.VERSION.SDK_INT >= 30) {
                if (!Environment.isExternalStorageManager()) {
                    manageFilePermissionDenied = false
                    permissionDeniedDialog?.show()
                } else {
                    showOption(binding.header.threeDot)
                }
            } else {
                showOption(binding.header.threeDot)
            }
        }


    private fun launchForManageFilePermission() {
        val getpermission = Intent()
        getpermission.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
        launcher.launch(getpermission)
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
            "output_${System.currentTimeMillis()}" + ".pdf",
            object : PdfPrint.CallbackPrint {
                override fun success(path: String) {
                    showToast("Saved")
                    article?.savedPath = path
                    article?.let {
                        saveArticle(it)
                    }
                }

                override fun onFailure() {
                    showToast("failed")
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
        article?.savedPath?.let {
            val f = File(it)
            Log.i("fileser", f.path.toString())
            binding.newPdf.fromFile(f).password(null).defaultPage(0).onPageError { page, e ->
                showToast(e.localizedMessage.toString())
            }.load()
        }

    }

}