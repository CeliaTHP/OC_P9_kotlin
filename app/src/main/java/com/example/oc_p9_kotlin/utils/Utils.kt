package com.example.oc_p9_kotlin.utils

import android.content.Context
import android.net.*
import android.net.wifi.WifiManager
import android.util.Log
import com.example.oc_p9_kotlin.models.InternetCallback
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class Utils {
    companion object {
        const val TAG: String = "Utils"
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
     fun isInternetAvailable(context: Context, internetCallback: InternetCallback){
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        connectivityManager.requestNetwork(
            networkRequest,
            object : ConnectivityManager.NetworkCallback() {
                // network is available for use
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    internetCallback.onInternetEnabled()
                }

                // Network capabilities have changed for the network
                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {

                    super.onCapabilitiesChanged(network, networkCapabilities)
                    internetCallback.onInternetConfigChanged()
                    val unmetered =
                        networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)


                }

                // lost network connection
                override fun onLost(network: Network) {
                    super.onLost(network)
                    internetCallback.onInternetDisabled()

                }
            })
    }


}