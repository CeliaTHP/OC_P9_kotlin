package com.example.oc_p9_kotlin.utils

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

class InternetUtils(
    private val connectivityManager: ConnectivityManager,
    private val operatingSystem: OperatingSystem
) {

    companion object {
        private const val TAG = "InternetUtils"
        private var instance: InternetUtils? = null

        fun getInstance(
            connectivityManager: ConnectivityManager,
            operatingSystem: OperatingSystem = OperatingSystem()
        ): InternetUtils {
            //classe ici ???
            return instance ?: synchronized(this) {
                createInstance(connectivityManager, operatingSystem)
            }
        }

        private fun createInstance(
            connectivityManager: ConnectivityManager,
            sdkUtils: OperatingSystem
        ): InternetUtils {
            val newInstance = InternetUtils(connectivityManager, sdkUtils)
            this.instance = newInstance
            return newInstance
        }


    }


    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param context
     * @return
     */
    fun isNetworkAvailable(): Boolean {
        if (operatingSystem.version >= Build.VERSION_CODES.M) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                    //TODO : ADD ALL WAYS OF HAVING INTERNET
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }


}