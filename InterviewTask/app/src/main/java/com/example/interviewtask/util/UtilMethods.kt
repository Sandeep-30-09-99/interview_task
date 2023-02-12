package com.example.interviewtask.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*


object UtilMethods {

    /*  @Inject
      lateinit var sharedPrefManager: SharedPrefManager


      fun logout(context: Activity) {
          sharedPrefManager.clearUser()
          val intent = Intent(context, LoginActivity::class.java)
          context.startActivity(intent)
          context.finishAffinity()
      }

  */

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    fun convertToCurrentTimeZone(date: String?): String? {
        return try {
            //yyyy-MM-dd'T'HH:mm:ss.SSSZ or yyyy-MM-dd'T'HH:mm:ss.SSSZZ:
            //YYYY-MM-DDThh:mm:ss.sTZD
            val utcFormat: DateFormat =
                if (date?.contains("+") == true) SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+hh:mm'")
                else if (date?.contains(".") == true) SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.s'Z'")
                else SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")

            val date = utcFormat.parse(date)

            val currentTFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm aa")
            currentTFormat.timeZone = TimeZone.getTimeZone(getCurrentTimeZone())
            currentTFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i("Ecsdfs", e.localizedMessage.toString())
            null
        }
    }

    fun getCurrentTimeZone(): String? {
        val tz: java.util.TimeZone = Calendar.getInstance().timeZone
        println(tz.displayName)
        return tz.id
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    fun getUtcTime(date: String): String {

        val s: DateFormat = SimpleDateFormat(
            "dd/MMM/yyyy" + " " + " HH:mm:ss"
        )
        val localTime = s.parse(date)
        s.timeZone = TimeZone.getTimeZone("GMT")

        /*Log.i(
            "Time IN Gmt : "
                    + s.format(localTime)
        )*/
        return s.format(localTime)
    }


    fun getTime(hour: Int, min: Int): String {
        var hour = hour
        var format = ""
        if (hour == 0) {
            hour += 12
            format = "AM"
        } else if (hour == 12) {
            format = "PM"
        } else if (hour > 12) {
            hour -= 12
            format = "PM"
        } else {
            format = "AM"
        }
        var hr = ""
        var minutes = ""

        hr = if (hour < 10) {
            "0$hour"
        } else {
            hour.toString()
        }


        minutes = if (min < 10) {
            "0$min"
        } else {
            min.toString()
        }

        return StringBuilder().append(hr).append(":").append(minutes).append(" ").append(format)
            .toString()
    }


    fun getProfileTextFromName(name: String?, lastName: String?): String {
        if (name != null && lastName != null) {
            return name[0] + lastName[0].toString()
        } else if (name != null && lastName == null) {
            return name[0] + name[1].toString()
        } else {
            return ""
        }

    }

    fun getMonth(month: Int): String {
        when (month.toString()) {
            "01", "1" -> {
                return "Jan"
            }
            "02", "2" -> {
                return "Feb"
            }
            "03", "3" -> {
                return "Mar"
            }
            "04", "4" -> {
                return "Apr"
            }
            "05", "5" -> {
                return "May"
            }
            "06", "6" -> {
                return "Jun"
            }
            "07", "7" -> {
                return "Jul"
            }
            "08", "8" -> {
                return "Aug"
            }
            "09", "9" -> {
                return "Sep"
            }
            "10" -> {
                return "Oct"
            }
            "11" -> {
                return "Nov"
            }
            "12" -> {
                return "Dec"
            }

        }
        return ""
    }


    fun imageToMultiPart(image: File): MultipartBody.Part {
        return MultipartBody.Part.createFormData(
            "image", image.name, RequestBody.create("file".toMediaTypeOrNull(), image)
        )
    }

    fun hasPermissions(context: Context?, permissions: Array<String>?): Boolean {
        if (context != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        context, permission!!
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }

    fun drawableToBitmap(drawable: Drawable): Bitmap? {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        var width = drawable.intrinsicWidth
        width = if (width > 0) width else 1
        var height = drawable.intrinsicHeight
        height = if (height > 0) height else 1
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }


}