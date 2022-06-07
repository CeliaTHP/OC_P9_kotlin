package com.example.oc_p9_kotlin.models

import android.content.Context
import android.location.Location
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.oc_p9_kotlin.R
import java.util.Date
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.io.Serializable

@Parcelize
@Entity
data class Estate(
    @PrimaryKey
    val id: String,
    var type: EstateType,
    var city: String,
    @ColumnInfo(name = "price_in_euros")
    var priceInEuros: Int,
    @ColumnInfo(name = "surface_in_square_meters")
    var surfaceInSquareMeters: Int,
    var rooms: Int,
    var bathrooms: Int,
    var bedrooms: Int,
    var address: String,
    var location: Location,
    @ColumnInfo(name = "nearby_places")
    var nearbyPlaces: List<FakePOI>?,
    @ColumnInfo(name = "is_near_station")
    var isNearStation: Boolean = false,
    @ColumnInfo(name = "is_near_pub")
    var isNearPub: Boolean = false,
    @ColumnInfo(name = "is_near_hostel")
    var isNearHostel: Boolean = false,
    @ColumnInfo(name = "is_near_hospital")
    var isNearHospital: Boolean = false,
    @ColumnInfo(name = "is_near_school")
    var isNearSchool: Boolean = false,
    @ColumnInfo(name = "is_near_park")
    var isNearPark: Boolean = false,
    @ColumnInfo(name = "is_near_restaurant")
    var isNearRestaurant: Boolean = false,
    @ColumnInfo(name = "is_near_other")
    var isNearOther: Boolean = false,
    var description: String,
    var medias: List<Media>?,
    @ColumnInfo(name = "media_count")
    var mediaCount: Int,
    var videos: List<Media>?,
    @ColumnInfo(name = "entry_date")
    var entryDate: Date,
    @ColumnInfo(name = "sale_date")
    var saleDate: Date?,
    @ColumnInfo(name = "is_available")
    var isAvailable: Boolean,
    @ColumnInfo(name = "assigned_agent_id")
    var assignedAgentName: String?

) : Parcelable, Serializable


enum class EstateType(val stringValue: Int) {
    HOUSE(R.string.type_house),
    APARTMENT(R.string.type_apartment),
    BUILDING(R.string.type_building),
    LOFT(R.string.type_loft),
    CASTLE(R.string.type_castle),
    BOAT(R.string.type_boat),
    MANSION(R.string.type_mansion),
    SITE(R.string.type_site),
    OTHER(R.string.type_other)
}

enum class Currency {
    DOLLAR,
    EURO
}







