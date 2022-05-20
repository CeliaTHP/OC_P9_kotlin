package com.example.oc_p9_kotlin.fakeapi

import android.location.Location
import com.example.oc_p9_kotlin.models.FakePOI
import com.example.oc_p9_kotlin.models.POIType

object FakePOIGenerator {


    private val locationGareDeLest = Location("0").apply {
        latitude = 48.87677974011204
        longitude = 2.359231924645815
    }
    var gareDeLest: FakePOI = FakePOI(
        "0",
        "Gare de l'est",
        "Description Gare de l'est Description Gare de l'est Description Gare de l'est",
        POIType.STATION,
        locationGareDeLest.latitude,
        locationGareDeLest.longitude
    )


    private val locationGareDuNord = Location("1").apply {
        latitude = 48.880734333383366
        longitude = 2.3573728110962957
    }

    var gareDuNord: FakePOI = FakePOI(
        "1",
        "Gare Du Nord",
        "Description Gare du Nord Description Gare du Nord Description Gare du Nord",
        POIType.STATION,
        locationGareDuNord.latitude,
        locationGareDuNord.longitude
    )

    private val locationButtesChaumont = Location("2").apply {
        latitude = 48.880903900375046
        longitude = 2.3827931645234157
    }
    var buttesChaumont: FakePOI =
        FakePOI(
            "2",
            "Parc des Buttes-Chaumont",
            "Description Buttes-Chaumont Description Buttes-Chaumont Description Buttes-Chaumont",
            POIType.PARK,
            locationButtesChaumont.latitude,
            locationButtesChaumont.longitude
        )


    private val locationEcoleSacreCoeur = Location("3").apply {
        latitude = 48.87609924029325
        longitude = 2.368873497517436
    }

    var ecoleSacreCoeur: FakePOI =
        FakePOI(
            "3",
            "École Sacré Coeur",
            "Description Sacré Coeur Description Sacré Coeur Description Sacré Coeur",
            POIType.SCHOOL,
            locationEcoleSacreCoeur.latitude,
            locationEcoleSacreCoeur.longitude
        )

    private val locationStation = Location("4").apply {
        latitude = 48.87609924029325
        longitude = 2.368873497517436
    }

    var station: FakePOI = FakePOI(
        "4",
        "Station Service",
        "Description Station Description Station Description Station",
        POIType.STATION,
        locationStation.latitude,
        locationStation.longitude
    )

    private val locationPizzeriaParis = Location("5").apply {
        latitude = 48.8780829182576
        longitude = 2.3655670812118346
    }

    var pizzeriaParis: FakePOI = FakePOI(
        "5",
        "Pizzeria",
        "Description Pizzeria Description Pizzeria Description Pizzeria",
        POIType.RESTAURANT,
        locationPizzeriaParis.latitude,
        locationPizzeriaParis.longitude
    )

    private val locationHopitalSaintLouis = Location("6").apply {
        latitude = 48.876052585644054
        longitude = 2.368457988030451
    }

    var hopitalSaintLouis: FakePOI =
        FakePOI(
            "6",
            "Hôpital Saint-Louis",
            "Description Hôpital Saint-Louis Description Hôpital Saint-Louis Description Hôpital Saint-Louis",
            POIType.HOSPITAL,
            locationHopitalSaintLouis.latitude,
            locationHopitalSaintLouis.longitude
        )

    private val locationHotelIbisGareDuNord = Location("7").apply {
        latitude = 48.88127393786386
        longitude = 2.3629219090228366
    }
    var ibisGareDuNord: FakePOI =
        FakePOI(
            "7",
            "Hôtel Ibis Gare Du Nord",
            "Description Ibis Gare Du Nord Description Ibis Gare Du Nord Description Ibis Gare Du Nord ",
            POIType.HOSTEL,
            locationHotelIbisGareDuNord.latitude,
            locationHotelIbisGareDuNord.longitude
        )

    private val locationBarParis = Location("8").apply {
        latitude = 48.87770924637794
        longitude = 2.362042704305381
    }

    var barParis: FakePOI = FakePOI(
        "8",
        "Bar",
        "Description bar Paris Description bar Paris Description bar Paris ",
        POIType.PUB,
        locationBarParis.latitude,
        locationBarParis.longitude
    )

    private val locationConseilPrudhomme = Location("9").apply {
        latitude = 48.88057071999095
        longitude = 2.3661611594414707
    }

    var conseilPrudhomme: FakePOI = FakePOI(
        "9",
        "Conseil Prud'homme",
        "Description bar Prudhomme Description Prudhomme Description Prudhomme ",
        POIType.OTHER,
        locationConseilPrudhomme.latitude,
        locationConseilPrudhomme.longitude
    )


}