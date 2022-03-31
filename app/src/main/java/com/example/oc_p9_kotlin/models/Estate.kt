package com.example.oc_p9_kotlin.models

import android.content.Context
import android.location.Location
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.oc_p9_kotlin.R
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity
data class Estate(
    @PrimaryKey
    val id: String,
    var type: EstateType,
    var city: String,
    @ColumnInfo(name = "price_in_dollars")
    var priceInEuros: Int,
    @ColumnInfo(name = "surface_in_square_meters")
    var surfaceInSquareMeters: Int,
    var rooms: Int,
    var bathrooms: Int,
    var bedrooms: Int,
    var address: String,
    //var nearbyPlaces : List<NearbyPlaces>
    var location: Location,
    var description: String,
    //var medias: List<Media>,
    @ColumnInfo(name = "entry_date")
    var entryDate: Date,
    @ColumnInfo(name = "sale_date")
    var saleDate: Date?,
    @ColumnInfo(name = "is_available")
    var isAvailable: Boolean,
    @ColumnInfo(name = "is_furnished")
    var isFurnished: Boolean,
    @ColumnInfo(name = "assigned_agent_id")
    var assignedAgentId: String?

) : Parcelable {

}

enum class EstateType {
    HOUSE,
    APARTMENT,
    BUILDING,
    LOFT,
    CASTLE,
    BOAT,
    MANSION,
    SITE,
    OTHER
}
