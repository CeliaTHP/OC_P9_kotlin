package com.example.oc_p9_kotlin.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.oc_p9_kotlin.daos.EstateDao
import com.example.oc_p9_kotlin.models.Estate

@Database(entities = [Estate::class], version = 1)
abstract class EstateDatabase : RoomDatabase() {

    abstract fun estateDao(): EstateDao

    // --- SINGLETON ---
    companion object {

        @Volatile
        private var INSTANCE: EstateDatabase? = null

        private const val DATABASE_NAME = "estate_database"

        fun getDatabase(context: Context): EstateDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        EstateDatabase::class.java,
                        DATABASE_NAME
                    ).build()
                INSTANCE = instance
                return instance
            }
        }

    }

}