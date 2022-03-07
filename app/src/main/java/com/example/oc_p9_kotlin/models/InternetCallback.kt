package com.example.oc_p9_kotlin.models

interface InternetCallback {
    fun onInternetEnabled()
    fun onInternetConfigChanged()
    fun onInternetDisabled()
}