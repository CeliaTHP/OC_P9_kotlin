package com.example.oc_p9_kotlin.view_models

import androidx.lifecycle.ViewModel
import com.example.oc_p9_kotlin.daos.EstateDao
import com.example.oc_p9_kotlin.models.Estate
import com.example.oc_p9_kotlin.utils.MySchedulers
import io.reactivex.rxjava3.core.Observable
import java.util.Date


class FiltersViewModel(
    val estateDao: EstateDao,
    val mySchedulers: MySchedulers
) : ViewModel() {

    companion object {
        private const val TAG = "FiltersViewModel"
    }

    fun getWithFilters(
        estateType: String,
        priceMin: Int,
        priceMax: Int,
        surfaceMin: Int,
        surfaceMax: Int,
        roomsMin: Int,
        roomsMax: Int,
        bathroomsMin: Int,
        bathroomsMax: Int,
        bedroomsMin: Int,
        bedroomsMax: Int,
        photosMin: Int,
        photosMax: Int,
        entryDate: Date,
        saleDate: Date?,
        isNearStation: Boolean,
        isNearPub: Boolean,
        isNearHostel: Boolean,
        isNearHospital: Boolean,
        isNearSchool: Boolean,
        isNearPark: Boolean,
        isNearRestaurant: Boolean,
        isNearOther: Boolean
    ): Observable<MutableList<Estate>> =
            estateDao.getWithFilters(
                estateType,
                priceMin,
                priceMax,
                surfaceMin,
                surfaceMax,
                roomsMin,
                roomsMax,
                bathroomsMin,
                bathroomsMax,
                bedroomsMin,
                bedroomsMax,
                photosMin,
                photosMax,
                entryDate,
                saleDate,
                isNearStation,
                isNearPub,
                isNearHostel,
                isNearHospital,
                isNearSchool,
                isNearPark,
                isNearRestaurant,
                isNearOther

            )
            .subscribeOn(mySchedulers.io)
            .observeOn(mySchedulers.main)

}