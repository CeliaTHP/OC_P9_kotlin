package com.example.oc_p9_kotlin.databases

import android.content.ContentValues
import android.content.Context
import android.location.Location
import android.telecom.Call
import android.util.Log
import androidx.room.Database
import androidx.room.OnConflictStrategy
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.oc_p9_kotlin.daos.EstateDao
import com.example.oc_p9_kotlin.fakeapi.FakeEstateApi
import com.example.oc_p9_kotlin.models.Estate
import com.example.oc_p9_kotlin.models.EstateType
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
                    )
                        //.addCallback(prepopulateDatabase())
                        .build()
                INSTANCE = instance
                return instance
            }
        }

        private fun prepopulateDatabase(): Callback {
            return object : Callback() {

                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    val contentValues = ContentValues().apply {
                        put("id", 0)
                        put("type", EstateType.APPARTMENT.name)
                        put("city", "PreParis")
                        put("price_in_dollars", 360000.0)
                        put("surface_in_square_meters", 50)
                        put("rooms", 4)
                        put("bathrooms", 1)
                        put("bedrooms", 2)
                        put("address", "00 green road 75001, PreParis")
                        put("latitude", 48.87868213644286)
                        put("longitude", 2.3641768593711796)
                        put(
                            "description",
                            "Super description from prepopulated database...Super description from prepopulated database...Super description from prepopulated database..." +
                                    "Super description from prepopulated database...Super description from prepopulated database...Super description from prepopulated database"
                        )
                        put("entry_date", Date().time)
                        put("isAvailable", true)
                        put("isFurnished", false)

                    }

                    db.insert(DATABASE_NAME, OnConflictStrategy.ABORT, contentValues)


                }
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








}