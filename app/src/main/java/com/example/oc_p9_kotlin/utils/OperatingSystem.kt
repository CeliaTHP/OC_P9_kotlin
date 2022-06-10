package com.example.oc_p9_kotlin.utils

import android.os.Build

data class OperatingSystem(
    val version: Int = Build.VERSION.SDK_INT
)
