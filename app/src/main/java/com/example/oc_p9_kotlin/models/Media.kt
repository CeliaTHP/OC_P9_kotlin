package com.example.oc_p9_kotlin.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Media(
    var id: String,
    var name: String,
    var url: String,
    var isLocal: Boolean = false

) : Parcelable {

}