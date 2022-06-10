package com.example.oc_p9_kotlin

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
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class InternetUtilsTests {

    @Before
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
            InternetUtils.createInstance(connectivityManagerMock, sdkUtilsMock).isNetworkAvailable()
        )

    }

    @Test
    fun isInternetAvailableTestSdk23Failure() {

        val connectivityManagerMock = Mockito.mock(ConnectivityManager::class.java)
        val networkCapabilitiesMock = Mockito.mock(NetworkCapabilities::class.java)
        val sdkUtilsMock = OperatingSystem(23)
        val activeNetworkMock = Mockito.mock(Network::class.java)

        whenever(connectivityManagerMock.activeNetwork).thenReturn(activeNetworkMock)
        whenever(connectivityManagerMock.getNetworkCapabilities(connectivityManagerMock.activeNetwork))
            .thenReturn(networkCapabilitiesMock)
        whenever(networkCapabilitiesMock.hasTransport(any()))
            .thenReturn(false)

        assertFalse(
            InternetUtils.createInstance(connectivityManagerMock, sdkUtilsMock).isNetworkAvailable()
        )

    }


    @Test
    fun isInternetAvailableTestSdk22Success() {

        val connectivityManagerMock = Mockito.mock(ConnectivityManager::class.java)
        val sdkUtilsMock = OperatingSystem(22)
        val activeNetworkInfoMock = Mockito.mock(NetworkInfo::class.java)

        whenever(connectivityManagerMock.activeNetworkInfo).thenReturn(activeNetworkInfoMock)
        whenever(activeNetworkInfoMock.isConnected).thenReturn(true)

        assertTrue(
            InternetUtils.createInstance(connectivityManagerMock, sdkUtilsMock).isNetworkAvailable()
        )

    }

    @Test
    fun isInternetAvailableTestSdk22Failure() {

        val connectivityManagerMock = Mockito.mock(ConnectivityManager::class.java)
        val sdkUtilsMock = OperatingSystem(22)
        val activeNetworkInfoMock = Mockito.mock(NetworkInfo::class.java)

        whenever(connectivityManagerMock.activeNetworkInfo).thenReturn(activeNetworkInfoMock)
        whenever(activeNetworkInfoMock.isConnected).thenReturn(false)

        assertFalse(
            InternetUtils.createInstance(connectivityManagerMock, sdkUtilsMock).isNetworkAvailable()
        )

    }

}