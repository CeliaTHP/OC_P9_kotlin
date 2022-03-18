package com.example.oc_p9_kotlin.databases

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.oc_p9_kotlin.daos.EstateDao
import com.example.oc_p9_kotlin.models.Estate
import com.google.gson.Gson
import java.util.Date

@Database(entities = [Estate::class], version = 1)
@TypeConverters(
    EstateDatabase.DateConverter::class,
    EstateDatabase.LocationConverter::class
)
abstract class EstateDatabase : RoomDatabase() {

    abstract fun estateDao(): EstateDao

    // --- SINGLETON ---
    companion object {

        private const val TAG = "EstateDatabase"
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

    internal class DateConverter {
        @TypeConverter
        fun toDate(timestamp: Long?): Date? {
            return when (timestamp) {
                null -> null
                else -> Date(timestamp)
            }
        }

        @TypeConverter
        fun fromDate(date: Date?): Long? {
            return date?.time
        }

    }

    internal class LocationConverter {
        @TypeConverter
        fun toLocation(locationString: String?): Location? {
            return try {
                Gson().fromJson(locationString, Location::class.java)
            } catch (e: Exception) {
                null
            }
        }

        @TypeConverter
        fun toLocationString(location: Location?): String? {
            return Gson().toJson(location)
        }
    }






}