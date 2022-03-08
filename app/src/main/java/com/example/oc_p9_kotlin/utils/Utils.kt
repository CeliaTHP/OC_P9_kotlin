package com.example.oc_p9_kotlin.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiManager
import android.os.Build
import android.util.Log
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class Utils {
    companion object {
        const val TAG: String = "Utils_"
    }

    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param dollars
     * @return
     */
    fun convertDollarToEuro(dollars: Int): Int {
        //was 0.812
        return Math.round(dollars * 0.88).toInt()
    }

    fun convertEuroToDollars(euros: Int): Int {
        //was 0.812
        return Math.round(euros / 0.88).toInt()
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return
     */
    fun getTodayDate(customFormat: CustomFormat): String? {
        val dateFormat: DateFormat = if (customFormat == CustomFormat.DAY_MONTH_YEAR) {
            SimpleDateFormat("dd/MM/yyyy")
        } else {
            SimpleDateFormat("yyyy/MM/dd")
        }
        return dateFormat.format(Date())
    }

    enum class CustomFormat {
        YEAR_MONTH_DAY,
        DAY_MONTH_YEAR
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param context
     * @return
     */

    //was isInternetAvailable
    fun isWifiAvailable(context: Context): Boolean? {
        val wifi = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifi.isWifiEnabled
    }


    //Verify if internet is enabled on the device
/*

    fun isInternetAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val capabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        return true
                    }
                }
            } else {
                try {
                    val activeNetworkInfo = connectivityManager.activeNetworkInfo
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                        Log.i("update_statut", "Network is available : true")
                        return true
                    }
                } catch (e: Exception) {
                    Log.i("update_statut", "" + e.message)
                }
            }
        }
        Log.i("update_statut", "Network is available : FALSE ")
        return false
    }


 */


    fun isInternetAvailable(context: Context) {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        //TODO : FIND WHY IT DOESN'T TRIGGER ANYTHING WHEN 4G & WIFI OFF

        connectivityManager.requestNetwork(
            networkRequest,
            object : ConnectivityManager.NetworkCallback() {
                // network is available for use
                override fun onAvailable(network: Network) {
                    //WIFI ONLY
                    super.onAvailable(network)
                    Log.d(TAG, "onAvailable")

                }

                // Network capabilities have changed for the network
                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    super.onCapabilitiesChanged(network, networkCapabilities)
                    val unmetered =
                        networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
                    Log.d(TAG, "onCapabilitesChanged $unmetered")


                }

                // lost network connection
                override fun onLost(network: Network) {
                    super.onLost(network)
                    Log.d(TAG, "onLost")

                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    Log.d(TAG, "onUnavailable "+ connectivityManager.isDefaultNetworkActive.toString())

                }

            })
    }



}