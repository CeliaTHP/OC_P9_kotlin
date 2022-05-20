package com.example.oc_p9_kotlin.models

import android.location.Location
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.oc_p9_kotlin.R
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class FakePOI(
    @PrimaryKey
    val id: String,
    var name: String,
    var description: String,
    var poiType: POIType,
    var latitude: Double,
    var longitude: Double

) : Parcelable

enum class POIType(val stringValue: Int) {

    STATION(R.string.poi_type_station),
    PUB(R.string.poi_type_pub),
    HOSTEL(R.string.poi_type_hotel),
    HOSPITAL(R.string.poi_type_hospital),
    SCHOOL(R.string.poi_type_school),
    PARK(R.string.poi_type_park),
    RESTAURANT(R.string.poi_type_restaurant),
    OTHER(R.string.poi_type_restaurant)


}