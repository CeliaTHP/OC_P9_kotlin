package com.example.oc_p9_kotlin.models

import android.content.ContentValues
import android.location.Location
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.oc_p9_kotlin.R
import kotlinx.parcelize.Parcelize
import java.io.Serializable
import java.util.Date

@Parcelize
@Entity
data class Estate(
    @PrimaryKey
    var id: String = "",
    var type: EstateType = EstateType.HOUSE,
    var city: String = "",
    @ColumnInfo(name = "price_in_euros")
    var priceInEuros: Int = 0,
    @ColumnInfo(name = "surface_in_square_meters")
    var surfaceInSquareMeters: Int = 0,
    var rooms: Int = 0,
    var bathrooms: Int = 0,
    var bedrooms: Int = 0,
    var address: String = "",
    var location: Location = Location(""),
    @ColumnInfo(name = "nearby_places")
    var nearbyPlaces: List<FakePOI>? = emptyList(),
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
    var description: String = "",
    var medias: List<Media>? = emptyList(),
    @ColumnInfo(name = "media_count")
    var mediaCount: Int = 0,
    var videos: List<Media>? = emptyList(),
    @ColumnInfo(name = "entry_date")
    var entryDate: Date = Date(),
    @ColumnInfo(name = "sale_date")
    var saleDate: Date? = null,
    @ColumnInfo(name = "is_available")
    var isAvailable: Boolean = true,
    @ColumnInfo(name = "assigned_agent_name")
    var assignedAgentName: String? = ""

) : Parcelable, Serializable {
    companion object {
        fun fromContentValues(contentValues: ContentValues): Estate {
            val estate = Estate()
            if (contentValues.containsKey("id")) estate.id = contentValues.getAsString("id")
            if (contentValues.containsKey("type")) estate.type =
                contentValues.get("type") as EstateType
            if (contentValues.containsKey("city")) estate.city = contentValues.getAsString("city")
            if (contentValues.containsKey("price_in_euros")) estate.priceInEuros =
                contentValues.getAsInteger("price_in_euros")
            if (contentValues.containsKey("surface_in_square_meters")) estate.surfaceInSquareMeters =
                contentValues.getAsInteger("surface_in_square_meters")
            if (contentValues.containsKey("rooms")) estate.rooms =
                contentValues.getAsInteger("rooms")
            if (contentValues.containsKey("bathrooms")) estate.bathrooms =
                contentValues.getAsInteger("bathrooms")
            if (contentValues.containsKey("bedrooms")) estate.bedrooms =
                contentValues.getAsInteger("bedrooms")
            if (contentValues.containsKey("address")) estate.address =
                contentValues.getAsString("address")
            if (contentValues.containsKey("location")) estate.location =
                contentValues.get("location") as Location
            if (contentValues.containsKey("nearby_places")) estate.nearbyPlaces =
                contentValues.get("nearby_places") as List<FakePOI>?
            if (contentValues.containsKey("is_near_station")) estate.isNearStation =
                contentValues.getAsBoolean("is_near_station")
            if (contentValues.containsKey("is_near_pub")) estate.isNearPub =
                contentValues.getAsBoolean("is_near_pub")
            if (contentValues.containsKey("is_near_hostel")) estate.isNearHostel =
                contentValues.getAsBoolean("is_near_hostel")
            if (contentValues.containsKey("is_near_hospital")) estate.isNearHospital =
                contentValues.getAsBoolean("is_near_hospital")
            if (contentValues.containsKey("is_near_school")) estate.isNearSchool =
                contentValues.getAsBoolean("is_near_school")
            if (contentValues.containsKey("is_near_park")) estate.isNearPark =
                contentValues.getAsBoolean("is_near_park")
            if (contentValues.containsKey("is_near_restaurant")) estate.isNearRestaurant =
                contentValues.getAsBoolean("is_near_restaurant")
            if (contentValues.containsKey("is_near_other")) estate.isNearOther =
                contentValues.getAsBoolean("is_near_other")
            if (contentValues.containsKey("description")) estate.description =
                contentValues.getAsString("description")
            if (contentValues.containsKey("medias")) estate.medias =
                contentValues.get("nearby_places") as List<Media>?
            if (contentValues.containsKey("media_count")) estate.mediaCount =
                contentValues.getAsInteger("media_count")
            if (contentValues.containsKey("videos")) estate.videos =
                contentValues.get("videos") as List<Media>?
            if (contentValues.containsKey("entry_date")) estate.entryDate =
                contentValues.get("entry_date") as Date
            if (contentValues.containsKey("sale_date")) estate.saleDate =
                contentValues.get("sale_date") as Date
            if (contentValues.containsKey("is_available")) estate.isAvailable =
                contentValues.getAsBoolean("is_available")
            if (contentValues.containsKey("assigned_agent_name")) estate.assignedAgentName =
                contentValues.getAsString("assigned_agent_name")
            return estate
        }
    }


}

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








