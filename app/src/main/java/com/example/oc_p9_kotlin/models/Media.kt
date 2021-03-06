package com.example.oc_p9_kotlin.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Media(
    var name: String,
    var uri: String,
) : Parcelable