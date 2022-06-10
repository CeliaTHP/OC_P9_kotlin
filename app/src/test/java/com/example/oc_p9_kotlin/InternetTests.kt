package com.example.oc_p9_kotlin

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import com.example.oc_p9_kotlin.utils.InternetUtils
import com.example.oc_p9_kotlin.utils.OperatingSystem
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class InternetTests {

    private val context: Context = Mockito.mock(Context::class.java)
    private val connectivityManager = Mockito.mock(ConnectivityManager::class.java)


    @Test
    fun isInternetAvailableTestSdk23Success() {

        val connectivityManagerMock = Mockito.mock(ConnectivityManager::class.java)
        val networkCapabilitiesMock = Mockito.mock(NetworkCapabilities::class.java)
        val sdkUtilsMock = OperatingSystem(23)
        val activeNetworkMock = Mockito.mock(Network::class.java)

        whenever(connectivityManagerMock.activeNetwork).thenReturn(activeNetworkMock)
        whenever(connectivityManagerMock.getNetworkCapabilities(connectivityManagerMock.activeNetwork))
            .thenReturn(networkCapabilitiesMock)
        whenever(networkCapabilitiesMock.hasTransport(any()))
            .thenReturn(true)

        assertTrue(
            InternetUtils.getInstance(connectivityManagerMock, sdkUtilsMock).isNetworkAvailable()
        )

    }

    @Test
    fun isInternetAvailableTestSdk23Failure() {

    }


    @Test
    fun isInternetAvailableTestSuccessMinSdkUnder29() {


    }


}