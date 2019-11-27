package com.example.demodata.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.Room
import com.example.demodata.model.Movie


@Database(entities = arrayOf(Movie::class), version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun orderLog(): RestoDao
    companion object {

        var INSTANCE: AppDatabase? = null



        fun getDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    if (INSTANCE == null) {
                        // Create database here
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java!!, "OrderLocalDB"
                        )
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}