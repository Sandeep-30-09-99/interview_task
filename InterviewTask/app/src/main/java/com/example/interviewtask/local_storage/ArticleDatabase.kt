package com.example.interviewtask.local_storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.interviewtask.model.Article
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Article::class], version = 4)
@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase() {

    abstract fun noteDao(): ArticleDao

    companion object {
        private var instance: ArticleDatabase? = null

        const val PRODUCT_DATABASE = "Product_Database"


        @Synchronized
        fun getInstance(ctx: Context): ArticleDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    ctx.applicationContext, ArticleDatabase::class.java,
                    PRODUCT_DATABASE
                )
                    .fallbackToDestructiveMigration()
                    //.addTypeConverter(TConverter::class.java)
                    .addCallback(roomCallback)
                    .build()

            }
            return instance!!

        }

        private val roomCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                populateDatabase(instance!!)
            }
        }

        private fun populateDatabase(db: ArticleDatabase) {
            val noteDao = db.noteDao()
            CoroutineScope(Dispatchers.IO).launch {
                //   noteDao.insert(Product(1, "Coffee", "Drinkable Product", 15.20F, 10F, null, null))
                //   noteDao.insert(Product(2, "Tea", "Drinkable Product", 15.20F, 10F, null, null))
            }
        }
    }


}