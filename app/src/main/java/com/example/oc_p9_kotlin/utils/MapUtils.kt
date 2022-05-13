package com.example.oc_p9_kotlin.utils

import com.example.oc_p9_kotlin.models.Estate
import org.osmdroid.bonuspack.location.NominatimPOIProvider
import org.osmdroid.bonuspack.location.POI
import org.osmdroid.util.GeoPoint

class MapUtils {

    fun getAllPois(estate: Estate): List<POI> {

        val poiProvider = NominatimPOIProvider("OSMBonusPackTutoUserAgent")
        val geoPoint = GeoPoint(estate.location.latitude, estate.location.longitude)

        val finalList = arrayListOf<POI>()

        val poisBank = poiProvider.getPOICloseTo(
            GeoPoint(
                geoPoint
            ), "bank", 10, 0.1
        )


        val poisCinema = poiProvider.getPOICloseTo(
            GeoPoint(
                geoPoint
            ), "cinema", 10, 0.1
        )
        val poisBar = poiProvider.getPOICloseTo(
            GeoPoint(
                geoPoint
            ), "bar", 10, 0.1
        )
        val poisCafe = poiProvider.getPOICloseTo(
            GeoPoint(
                geoPoint
            ), "cafe", 10, 0.1
        )

        val poisCasino = poiProvider.getPOICloseTo(
            GeoPoint(
                geoPoint
            ), "casino", 10, 0.1
        )
        val poisRestaurant = poiProvider.getPOICloseTo(
            GeoPoint(
                geoPoint
            ), "restaurant", 10, 0.1
        )

        val poisFastFood = poiProvider.getPOICloseTo(
            GeoPoint(
                geoPoint
            ), "fast_food", 10, 0.1
        )
        val poisFuel = poiProvider.getPOICloseTo(
            GeoPoint(
                geoPoint
            ), "fuel", 10, 0.1
        )
        val poisNightClub = poiProvider.getPOICloseTo(
            GeoPoint(
                geoPoint
            ), "nightclub", 10, 0.1
        )

        val poisTheatre = poiProvider.getPOICloseTo(
            GeoPoint(
                geoPoint
            ), "theatre", 10, 0.1
        )


        finalList.addAll(poisBank)
        finalList.addAll(poisCinema)
        finalList.addAll(poisBar)
        finalList.addAll(poisCafe)
        finalList.addAll(poisCasino)
        finalList.addAll(poisRestaurant)
        finalList.addAll(poisFastFood)
        finalList.addAll(poisFuel)
        finalList.addAll(poisNightClub)

        return finalList
    }
}