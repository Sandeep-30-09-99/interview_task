package com.example.interviewtask.local_storage

import androidx.room.TypeConverter
import com.example.interviewtask.model.Source
import com.google.gson.Gson

class Converters {

    var gson = Gson()

    @TypeConverter
    fun stringToSource(data: String?): Source? {
        if (data == null) {
            return Source("", "")
        }
        return gson.fromJson<Source>(data, Source::class.java)
    }

    @TypeConverter
    fun sourceToString(someObjects: Source): String? {
        return gson.toJson(someObjects)
    }

}