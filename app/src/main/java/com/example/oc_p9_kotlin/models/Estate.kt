package com.example.oc_p9_kotlin.models

import android.location.Location
import java.util.*

data class Estate(
    val id: String,
    var type: EstateType,
    var city: String,
    var priceInDollars: Double,
    var surfaceInSquareMeters: Int,
    var rooms: Int,
    var bathrooms: Int,
    var bedrooms: Int,
    var address: String,
    //var nearbyPlaces : List<NearbyPlaces>
    var location: Location,
    var description: String,
    //var medias: List<Media>
    var entryDate: Date,
    var saleDate: Date?,
    var isAvailable: Boolean,
    var isFurnished: Boolean,
    var assignedAgentId: String?

) {

}
enum class EstateType {
    HOUSE,
    APPARTMENT,
    BUILDING,
    LOFT,
    CASTLE,
    BOAT,
    MANSION,
    SITE,
    OTHER

}