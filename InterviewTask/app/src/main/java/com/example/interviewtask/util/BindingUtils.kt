package com.example.interviewtask.util

import android.os.Build
import android.text.Html
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.interviewtask.R
import java.text.SimpleDateFormat

object BindingUtils {



    @BindingAdapter("set_time")
    @JvmStatic
    fun setLocalDateTime(view: TextView, utcDateTime: String?) {

        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                view.text = UtilMethods.convertToCurrentTimeZone(utcDateTime)
            }
        } catch (e: Exception) {
            Log.i("Exception", e.localizedMessage.toString())
        }
    }

    @BindingAdapter("set_html_text")
    @JvmStatic
    fun setHtmlTe(textiew: TextView, content: String?) {
        textiew.text = Html.fromHtml(content)
    }


    @BindingAdapter("set_img")
    @JvmStatic
    fun setImg(view: ImageView, urlToImage: String?) {
        Glide.with(view.context)
            .load(urlToImage)
            .placeholder(R.drawable.ic_img_not_available)
            .into(view)
    }
}