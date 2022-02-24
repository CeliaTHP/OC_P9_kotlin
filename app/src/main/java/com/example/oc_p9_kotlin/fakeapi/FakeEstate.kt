package com.example.oc_p9_kotlin.fakeapi

import android.location.Location
import com.example.oc_p9_kotlin.models.Estate
import com.example.oc_p9_kotlin.models.EstateType
import java.text.SimpleDateFormat
import java.util.*

object FakeEstateApi {

    public fun getFakeEstateList(): List<Estate> {
        return DUMMY_ESTATES
    }


    private val location0 = Location("0").apply {
        latitude = 48.87868213644286
        longitude = 2.3641768593711796
    }

    private val location1 = Location("1").apply {
        latitude = 47.80186345753006
        longitude = 1.9606768965440708
    }

    private val location2 = Location("2").apply {
        latitude = 48.6541140293708
        longitude = -2.0156675439089335
    }

    private val location3 = Location("3").apply {
        latitude = 44.240740296974444
        longitude = 3.0155654515106396
    }

    private val location4 = Location("4").apply {
        latitude = 48.89499756434587
        longitude = 2.3791019838878382
    }


    private val DUMMY_ESTATES: List<Estate> = listOf(
        Estate(
            "0",
            EstateType.APPARTMENT,
            "Paris",
            360000.0,
            50,
            4,
            1,
            2,
            "0 green road 75001, Paris",
            location0,
            "The house itself is surrounded by a tranquil garden, with various flowers, a long pond including a small waterfall and various rock formations.Large, octagon windows brighten up the house and have been added to the house in a fairly symmetrical pattern." +
                    "The house is equipped with a large kitchen and one average bathroom, it also has a large living room, five bedrooms, a cozy dining room," +
                    " a lounge area and a cozy storage room.",
            Date(),
            null,
            isAvailable = true,
            isFurnished = true,
            null
        ),

        Estate(
            "1",
            EstateType.SITE,
            "Saint-Cyr",
            200000.0,
            90,
            0,
            0,
            0,
            "1 green road 45590, Saint-Cyr",
            location1,
            "From the outside this house looks warm and cozy. It has been built with cypress wood and has burgandy brick decorations. Tall, rectangular windows brighten up the house and have been added to the house in a mostly symmetric way." +
                    " The house is equipped with a huge kitchen and one large bathroom, it also has a cozy living room, two bedrooms, a modest dining room, an office and a cozy garage.",
            Date(),
            null,
            isAvailable = true,
            isFurnished = false,
            null
        ),


        Estate(
            "2",
            EstateType.BOAT,
            "Saint-Malo",
            60000.0,
            16,
            1,
            1,
            1,
            "2 green road 35400, Saint-Malo",
            location2,
            "The roof is low and pyramid shaped and is covered with brown ceramic tiles. Two large chimneys poke out the center of the roof. Several long, thin windows let in plenty of light to the rooms below the roof.\n" +
                    "The house itself is surrounded by a gorgeous garden, including hanging grape vines, a pagoda, a pond and many different flowers..",
            Date(),
            null,
            isAvailable = true,
            isFurnished = false,
            null
        ),

        Estate(
            "3",
            EstateType.CASTLE,
            "Verrières",
            800000.0,
            200,
            8,
            3,
            5,
            "3 green road 78320, Verrières",
            location3,
            "The house is equipped with a large kitchen and three bathrooms, it also has a huge living room, five bedrooms," +
                    " a grand dining room, a sun room and a roomy garage.",
            Date(),
            null,
            isAvailable = true,
            isFurnished = true,
            null
        ),

        Estate(
            "4",
            EstateType.APPARTMENT,
            "Paris",
            60000.0,
            28,
            2,
            1,
            1,
            "4 green road 75019, Paris",
            location4,
            "From the outside this house looks impressive. It has been built with tan bricks and has oak wooden decorations. Short, wide windows allow enough light to enter the home and have been added to the house in a fairly asymmetrical pattern." +
                    " The house is equipped with a modern kitchen and one modern bathroom, it also has a large living room, three bedrooms, a large dining room and a small garage.",
            Date(),
            null,
            isAvailable = true,
            isFurnished = false,
            null
        )


    )


}