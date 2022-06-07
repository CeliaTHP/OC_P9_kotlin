package com.example.oc_p9_kotlin.databases

import android.content.Context
import android.location.Location
import android.net.Uri
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.oc_p9_kotlin.daos.EstateDao
import com.example.oc_p9_kotlin.models.Estate
import com.example.oc_p9_kotlin.models.FakePOI
import com.example.oc_p9_kotlin.models.Media
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.Date


@Database(entities = [Estate::class], version = 1)
@TypeConverters(
    EstateDatabase.DateConverter::class,
    EstateDatabase.LocationConverter::class,
    EstateDatabase.MediaListConverter::class,
    EstateDatabase.PoiListConverter::class,
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
                    )
                        .build()
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
        fun locationToString(location: Location?) =
            location?.let { "${it.latitude};${it.longitude}" }

        @TypeConverter
        fun stringToLocation(location: String?) = location?.let {
            val pieces = location.split(';')

            if (pieces.size == 2) {
                try {
                    Location(null as String?).apply {
                        latitude = pieces[0].toDouble()
                        longitude = pieces[1].toDouble()
                    }
                } catch (e: Exception) {
                    null
                }
            } else {
                null
            }
        }
    }

    class MediaListConverter {
        @TypeConverter
        fun fromMediaList(medias: List<Media?>?): String? {
            if (medias == null) {
                return null
            }
            val type: Type = object : TypeToken<List<Media?>?>() {}.type
            return Gson().toJson(medias, type)
        }

        @TypeConverter
        fun toMediaList(medias: String?): List<Media>? {
            if (medias == null) {
                return null
            }
            val type: Type = object : TypeToken<List<Media?>?>() {}.type
            return Gson().fromJson<List<Media>>(medias, type)
        }
    }

    class PoiListConverter {
        @TypeConverter
        fun fromPoiList(pois: List<FakePOI?>?): String? {
            if (pois == null) {
                return null
            }
            val gson = Gson()
            val type = object :
                TypeToken<List<FakePOI?>?>() {}.type
            return gson.toJson(pois, type)
        }

        @TypeConverter
        fun toPoiList(pois: String?): List<FakePOI>? {
            if (pois == null) {
                return null
            }
            val gson = Gson()
            val type = object :
                TypeToken<List<FakePOI?>?>() {}.type
            return gson.fromJson<List<FakePOI>>(pois, type)
        }
    }

}