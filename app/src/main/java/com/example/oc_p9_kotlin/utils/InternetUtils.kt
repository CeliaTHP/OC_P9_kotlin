package com.example.oc_p9_kotlin.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.util.Log

object InternetUtils {

    private const val TAG = "InternetUtils"
    private var networkIdList = mutableListOf<Network>()

    private var networkCallback = object : ConnectivityManager.NetworkCallback() {
        // network is available for use
        override fun onAvailable(network: Network) {
            //WIFI ONLY
            super.onAvailable(network)
            networkIdList.add(network)

            Log.d(TAG, "onAvailable" + network)

        }

        // Network capabilities have changed for the network
        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            val unmetered =
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
            Log.d(TAG, "onCapabilitesChanged" + network)
            Log.d(TAG, "onCapabilitesChanged $networkCapabilities")


        }

        // lost network connection
        override fun onLost(network: Network) {
            super.onLost(network)
            networkIdList.remove(network)

            Log.d(TAG, "onLost" + network.toString())

            if (networkIdList.isEmpty()) {
                Log.d(TAG, "EMPTY ID LIST ")
                // L
            }
        }

        override fun onUnavailable() {
            super.onUnavailable()
            Log.d(TAG, "onUnavailable")

        }

    }


    fun unregisterNetworkCallback(context: Context) {

        val connectivityManager =
            context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.unregisterNetworkCallback(networkCallback)

    }

    fun registerNetworkCallback(context: Context) {
        val connectivityManager =
            context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkRequest =
            NetworkRequest.Builder().apply {
                addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                addTransportType(NetworkCapabilities.TRANSPORT_BLUETOOTH)
                addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
                addTransportType(NetworkCapabilities.TRANSPORT_VPN)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                    addTransportType(NetworkCapabilities.TRANSPORT_LOWPAN)
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    addTransportType(NetworkCapabilities.TRANSPORT_WIFI_AWARE)
                    addTransportType(NetworkCapabilities.TRANSPORT_USB)
                }
            }
                .build()

        connectivityManager.registerNetworkCallback(
            networkRequest,
            networkCallback,
        )

    }


}