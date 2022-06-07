package com.example.oc_p9_kotlin.view_models

import androidx.lifecycle.ViewModel
import com.example.oc_p9_kotlin.daos.EstateDao
import com.example.oc_p9_kotlin.fakeapi.FakeEstateApi
import com.example.oc_p9_kotlin.models.Estate
import com.example.oc_p9_kotlin.models.EstateType
import com.example.oc_p9_kotlin.utils.MySchedulers
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
        if (saleDate != null) {
            estateDao.getWithFiltersWithSaleDate(
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
        } else {
            estateDao.getWithFiltersWithoutSaleDate(
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
                isNearStation,
                isNearPub,
                isNearHostel,
                isNearHospital,
                isNearSchool,
                isNearPark,
                isNearRestaurant,
                isNearOther
            )

        }
            .subscribeOn(mySchedulers.io)
            .observeOn(mySchedulers.main)

    /*
    fun generateData() {
        executor.execute {
            estateDao.insertAllEstates(FakeEstateApi.getFakeEstateList())
        }


    }

    fun getAll(): Observable<MutableList<Estate>> =
        estateDao.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getByType(estateType: EstateType): Observable<MutableList<Estate>> =
        estateDao.getByType(estateType)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
     */

}