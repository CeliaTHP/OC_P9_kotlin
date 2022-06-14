package com.example.oc_p9_kotlin

import android.location.Location
import com.example.oc_p9_kotlin.models.Estate
import com.example.oc_p9_kotlin.models.EstateType
import com.example.oc_p9_kotlin.models.FakePOI
import com.example.oc_p9_kotlin.models.POIType
import org.mockito.Mockito
import java.util.Date

class TestEstateList {

    fun getList(estateType: EstateType = EstateType.BOAT): List<Estate> {
        return listOf(
            Estate(
                "testId",
                estateType,
                "testCity",
                420000,
                42,
                1,
                2,
                3,
                "42 test address",
                Mockito.mock(Location::class.java),
                getFakePoiList(),
                isNearStation = false,
                isNearPub = false,
                isNearHostel = false,
                isNearHospital = false,
                isNearSchool = false,
                isNearPark = false,
                isNearRestaurant = true,
                isNearOther = false,
                "testDescription",
                null,
                0,
                null,
                Date(),
                null,
                true,
                "testAgentName"
            )
        )
    }

    private fun getFakePoiList(): List<FakePOI> {
        return listOf(
            FakePOI(
                "testId",
                "testName",
                "testDescription",
                POIType.RESTAURANT,
                66.6,
                42.2
            )
        )
    }
}