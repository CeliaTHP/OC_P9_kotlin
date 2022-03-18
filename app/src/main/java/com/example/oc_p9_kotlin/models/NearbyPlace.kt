package com.example.oc_p9_kotlin.models

import android.location.Location
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NearbyPlace(
    var id: String,
    var name: String,
    var location: Location,
    // Use default placeType for place ?
   // var placeType: PlaceType


    ) : Parcelable {}

enum class PlaceType {
    STORE,
    SCHOOL,
    PARK,
    RESTAURANT,
    BANK
}
