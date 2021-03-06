package com.example.oc_p9_kotlin.view_models

import androidx.lifecycle.ViewModel
import com.example.oc_p9_kotlin.daos.EstateDao
import com.example.oc_p9_kotlin.fake_apis.FakeEstateApi
import com.example.oc_p9_kotlin.models.Estate
import com.example.oc_p9_kotlin.models.EstateType
import com.example.oc_p9_kotlin.utils.MySchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single


class MainViewModel(
    val estateDao: EstateDao,
    private val mySchedulers: MySchedulers
) : ViewModel() {

    companion object {
        private const val TAG = "MainViewModel"
    }

    fun generateData(): Completable {
        return estateDao.insertAllEstates(FakeEstateApi.getFakeEstateList())
            .subscribeOn(mySchedulers.io)
            .observeOn(mySchedulers.main)
    }

    fun getAll(): Observable<MutableList<Estate>> =
        estateDao.getAll()
            .subscribeOn(mySchedulers.io)
            .observeOn(mySchedulers.main)

    fun getByType(estateType: EstateType): Observable<MutableList<Estate>> =
        estateDao.getByType(estateType)
            .subscribeOn(mySchedulers.io)
            .observeOn(mySchedulers.main)

}