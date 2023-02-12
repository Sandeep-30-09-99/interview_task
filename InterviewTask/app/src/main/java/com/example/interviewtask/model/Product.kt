package com.example.interviewtask.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.interviewtask.local_storage.Converters
import kotlinx.android.parcel.Parcelize

/*
Class = Product
Fields
id
name
description
regular price
sale price
product photo (image)
colors (array)    loadFromFile =
stores (dictionary)
*/

@Parcelize
data class Product(
    val id: Int? = 0,
    val name: String?,
    val description: String?,
    val regular_price: Float?,
    val sale_price: Float?,
    val product_photo: String?,
    val colors: String?
) : Parcelable