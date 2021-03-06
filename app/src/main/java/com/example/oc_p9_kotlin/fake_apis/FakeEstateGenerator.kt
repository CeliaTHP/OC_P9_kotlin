package com.example.oc_p9_kotlin.fake_apis

import android.location.Location
import com.example.oc_p9_kotlin.models.Estate
import com.example.oc_p9_kotlin.models.EstateType
import com.example.oc_p9_kotlin.models.Media
import java.util.*

object FakeEstateApi {

    fun getFakeEstateList(): List<Estate> {
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

    private var location5 = Location("5").apply {
        latitude = 48.89219394766597
        longitude = 2.2218665353504528
    }

    private var location6 = Location("6").apply {
        latitude = 49.00061043126319
        longitude = 1.6919191257189081
    }

    private var location7 = Location("7").apply {
        latitude = 45.680565545670596
        longitude = 1.0997837831349402
    }

    private var location8 = Location("8").apply {
        latitude = 46.09428662002291
        longitude = 6.053419269568852
    }

    private var location9 = Location("9").apply {
        latitude = 48.49467907808304
        longitude = 6.996455269621434
    }


    private fun getEntryDate(): Date {
        val cal = Calendar.getInstance()
        cal.time = Date()
        cal.add(Calendar.DATE, -10)
        return cal.time
    }

    private fun getSaleDate(): Date {
        val cal = Calendar.getInstance()
        cal.time = Date()
        cal.add(Calendar.DATE, -1)
        return cal.time
    }

    private val DUMMY_ESTATES: List<Estate> = listOf(
        Estate(
            "0",
            EstateType.APARTMENT,
            "Paris",
            360000,
            50,
            4,
            1,
            2,
            "0 green road 75001, Paris",
            location0,
            listOf(
                FakePOIGenerator.gareDeLest,
                FakePOIGenerator.gareDuNord,
                FakePOIGenerator.buttesChaumont,
                FakePOIGenerator.ecoleSacreCoeur,
                FakePOIGenerator.station,
                FakePOIGenerator.pizzeriaParis,
                FakePOIGenerator.hopitalSaintLouis,
                FakePOIGenerator.ibisGareDuNord,
                FakePOIGenerator.barParis,
                FakePOIGenerator.conseilPrudhomme
            ),
            isNearStation = true,
            isNearPub = true,
            isNearHostel = true,
            isNearHospital = true,
            isNearSchool = true,
            isNearPark = true,
            isNearRestaurant = true,
            isNearOther = true,
            description = "The house itself is surrounded by a tranquil garden, with various flowers, a long pond including a small waterfall and various rock formations.Large, octagon windows brighten up the house and have been added to the house in a fairly symmetrical pattern." +
                    "The house is equipped with a large kitchen and one average bathroom, it also has a large living room, five bedrooms, a cozy dining room," +
                    " a lounge area and a cozy storage room.",
            medias = listOf(
                Media("first pic", "https://imgur.com/X4YUxC8.png"),
                Media("second pic", "https://imgur.com/iuf7Olz.png"),
                Media("third pic", "https://imgur.com/CltG3DI.png"),
                Media("fourth pic", "https://imgur.com/PAVM3nU.png")

            ),
            mediaCount = 4,
            videos = null,
            entryDate = getEntryDate(),
            saleDate = null,
            isAvailable = true,
            assignedAgentName = null
        ),

        Estate(
            "1",
            EstateType.SITE,
            "Saint-Cyr",
            200000,
            90,
            0,
            0,
            0,
            "1 green road 45590, Saint-Cyr",
            location1,
            null,
            isNearStation = false,
            isNearPub = false,
            isNearHostel = false,
            isNearHospital = false,
            isNearSchool = false,
            isNearPark = false,
            isNearRestaurant = false,
            isNearOther = false,
            description = "From the outside this house looks warm and cozy. It has been built with cypress wood and has burgandy brick decorations. Tall, rectangular windows brighten up the house and have been added to the house in a mostly symmetric way." +
                    " The house is equipped with a huge kitchen and one large bathroom, it also has a cozy living room, two bedrooms, a modest dining room, an office and a cozy garage.",
            medias = listOf(
                Media("first pic", "https://imgur.com/1mUoNjw.png"),
                Media("second pic", "https://imgur.com/10h2bTP.png"),
                Media("third pic", "https://imgur.com/tRsPwQl.png")

            ),
            mediaCount = 2,
            videos = listOf(
                Media(
                    "first video",
                    "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
                )
            ),
            entryDate = getEntryDate(),
            saleDate = null,
            isAvailable = true,
            assignedAgentName = "Simon Blue"
        ),


        Estate(
            "2",
            EstateType.BOAT,
            "Saint-Malo",
            60000,
            16,
            1,
            1,
            1,
            "2 green road 35400, Saint-Malo",
            location2,
            null,
            isNearStation = false,
            isNearPub = false,
            isNearHostel = false,
            isNearHospital = false,
            isNearSchool = false,
            isNearPark = false,
            isNearRestaurant = false,
            isNearOther = false,

            description = "The roof is low and pyramid shaped and is covered with brown ceramic tiles. Two large chimneys poke out the center of the roof. Several long, thin windows let in plenty of light to the rooms below the roof.\n" +
                    "The house itself is surrounded by a gorgeous garden, including hanging grape vines, a pagoda, a pond and many different flowers..",
            medias = listOf(
                Media("first pic", "https://imgur.com/B9V6Czp.png"),
                Media("second pic", "https://imgur.com/vKEgG9u.png"),
                Media("third pic", "https://imgur.com/7bwxd4s.png"),
                Media("fourth pic", "https://imgur.com/2YoMmQO.png"),
                Media("fifth pic", "https://imgur.com/vJWTa6M.png"),
                Media("sixth pic", "https://imgur.com/RzVDiWH.png"),
                Media("seventh pic", "https://imgur.com/WntjHmD.png")
            ),
            mediaCount = 7,
            videos = null,
            entryDate = getEntryDate(),
            saleDate = null,
            isAvailable = true,
            assignedAgentName = null
        ),

        Estate(
            "3",
            EstateType.CASTLE,
            "Verri??res",
            800000,
            200,
            8,
            3,
            5,
            "3 green road 78320, Verri??res",
            location3,
            null,
            isNearStation = false,
            isNearPub = false,
            isNearHostel = false,
            isNearHospital = false,
            isNearSchool = false,
            isNearPark = false,
            isNearRestaurant = false,
            isNearOther = false,

            description = "The house is equipped with a large kitchen and three bathrooms, it also has a huge living room, five bedrooms," +
                    " a grand dining room, a sun room and a roomy garage.",
            medias = listOf(
                Media("first pic", "https://imgur.com/jVwBWXf.png"),
            ),
            mediaCount = 1,
            videos = null,
            entryDate = getEntryDate(),
            saleDate = getSaleDate(),
            isAvailable = false,
            assignedAgentName = null
        ),

        Estate(
            "4",
            EstateType.APARTMENT,
            "Paris",
            20000,
            28,
            2,
            1,
            1,
            "4 green road 75019, Paris",
            location4,
            listOf(FakePOIGenerator.barParis),
            isNearStation = false,
            isNearPub = true,
            isNearHostel = false,
            isNearHospital = false,
            isNearSchool = false,
            isNearPark = false,
            isNearRestaurant = false,
            isNearOther = false,
            description = "From the outside this house looks impressive. It has been built with tan bricks and has oak wooden decorations. Short, wide windows allow enough light to enter the home and have been added to the house in a fairly asymmetrical pattern." +
                    " The house is equipped with a modern kitchen and one modern bathroom, it also has a large living room, three bedrooms, a large dining room and a small garage.",
            medias = null,
            mediaCount = 0,
            videos = null,
            entryDate = getEntryDate(),
            saleDate = null,
            isAvailable = true,
            assignedAgentName = null
        ),
        Estate(
            "5",
            EstateType.LOFT,
            "Nanterre",
            72000,
            32,
            3,
            1,
            2,
            "5 green road 92000, Nanterre",
            location5,
            null,
            isNearStation = false,
            isNearPub = false,
            isNearHostel = false,
            isNearHospital = false,
            isNearSchool = false,
            isNearPark = false,
            isNearRestaurant = false,
            isNearOther = false,
            description = "The building is shaped like an L. The extension extends into a covered patio circling around half the house." +
                    "The second floor is the same size as the first, but part of it hangs over the edge of the floor below, creating an overhang on one side and a balcony on the other." +
                    "This floor has a very different style than the floor below.",
            medias = listOf(
                Media("first pic", "https://imgur.com/aPuQBbO.png"),
                Media("second pic", "https://imgur.com/NFAZ5ht.png"),
                Media("third pic", "https://imgur.com/YLA8Q0X.png"),
            ),
            mediaCount = 2,
            videos = null,
            entryDate = getEntryDate(),
            saleDate = null,
            isAvailable = true,
            assignedAgentName = null
        ),
        Estate(
            "6",
            EstateType.BUILDING,
            "Mantes-la-Jolie",
            320000,
            52,
            6,
            3,
            5,
            "6 green road 78200, Mantes-La-Jolie",
            location6,
            null,
            isNearStation = false,
            isNearPub = false,
            isNearHostel = false,
            isNearHospital = false,
            isNearSchool = false,
            isNearPark = false,
            isNearRestaurant = false,
            isNearOther = false,
            description = "From the outside this house looks stylish. It has been built with yellow pine wood and has white cedar wooden decorations." +
                    "Tall, half rounded windows add to the overall style of the house and have been added to the house in a mostly symmetric way.",
            medias = listOf(
                Media("first pic", "https://imgur.com/2cG7sb4.png"),
            ),
            mediaCount = 1,
            videos = listOf(
                Media(
                    "first video",
                    "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4"
                ),
                Media(
                    "second video",
                    "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackOnStreetAndDirt.mp4"
                ),
                Media(
                    "third video",
                    "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4"
                )


            ),
            entryDate = getEntryDate(),
            saleDate = null,
            isAvailable = true,
            assignedAgentName = null
        ),
        Estate(
            "7",
            EstateType.HOUSE,
            "Les Cars",
            650000,
            60,
            5,
            2,
            4,
            "7 green road 87230, Les Cars",
            location7,
            null,
            isNearStation = false,
            isNearPub = false,
            isNearHostel = false,
            isNearHospital = false,
            isNearSchool = false,
            isNearPark = false,
            isNearRestaurant = false,
            isNearOther = false,
            description = "The roof is flat and is covered with seagrass. One small chimney pokes out the center of the roof. Large, skylight windows let in plenty of light to the rooms below the roof." +
                    "The house itself is surrounded by paved ground, with various party spots, like a fancy barbeque and a firepit.",
            medias = null,
            mediaCount = 0,
            videos = null,
            entryDate = getEntryDate(),
            saleDate = getSaleDate(),
            isAvailable = false,
            assignedAgentName = "Victoria Purple"
        ),

        Estate(
            "8",
            EstateType.MANSION,
            "Feig??res",
            2300000,
            120,
            12,
            2,
            10,
            "8 green road 74160, Feig??res",
            location8,
            null,
            isNearStation = false,
            isNearPub = false,
            isNearHostel = false,
            isNearHospital = false,
            isNearSchool = false,
            isNearPark = false,
            isNearRestaurant = false,
            isNearOther = false,
            description = "The building is shaped like a squared S. The two extensions extend into wooden overhanging panels circling around half the house." +
                    "The second floor is the same size as the first, which has been built exactly on top of the floor below it. This floor has a very different style than the floor below.",
            medias = listOf(
                Media("first pic", "https://imgur.com/1hSWQTq.png"),
                Media("second pic", "https://imgur.com/u9FY0Dc.png"),
                Media("third pic", "https://imgur.com/RRWOGrY.png"),
            ),
            mediaCount = 3,
            videos = null,
            entryDate = getEntryDate(),
            saleDate = null,
            isAvailable = true,
            assignedAgentName = null
        ),

        Estate(
            "9",
            EstateType.OTHER,
            "Angomont",
            65000,
            20,
            3,
            1,
            1,
            "8 green road 54540, Angomont",
            location9,
            null,
            isNearStation = false,
            isNearPub = false,
            isNearHostel = false,
            isNearHospital = false,
            isNearSchool = false,
            isNearPark = false,
            isNearRestaurant = false,
            isNearOther = false,
            description = "The roof is low and slanted to one side and is covered with seagrass. One large chimney sits at the side of the house. Large, skylight windows let in plenty of light to the rooms below the roof." +
                    "The house itself is surrounded by a gorgeous garden, including various trees, bushes, flowers and a large pond.",
            medias = listOf(
                Media("first pic", "https://imgur.com/wTKdKsT.png"),
                Media("second pic", "https://imgur.com/gnD4qxI.png"),
                Media("third pic", "https://imgur.com/dx5TlJO.png"),
                Media("fourth pic", "https://imgur.com/ozijiGD.png"),
                Media("fifth pic", "https://imgur.com/HYjR1LG.png"),
                Media("sixth pic", "https://imgur.com/TqCmgAN.png"),

                ),
            mediaCount = 6,
            videos = null,
            entryDate = getEntryDate(),
            saleDate = getSaleDate(),
            isAvailable = false,
            assignedAgentName = null
        )

    )

}