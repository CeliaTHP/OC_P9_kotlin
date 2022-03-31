package com.example.oc_p9_kotlin.view_models

import android.content.Context
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oc_p9_kotlin.daos.EstateDao
import com.example.oc_p9_kotlin.databases.EstateDatabase
import com.example.oc_p9_kotlin.fakeapi.FakeEstateApi
import com.example.oc_p9_kotlin.models.Estate
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


class MainViewModel(val estateDao: EstateDao) : ViewModel() {

    companion object {
        private const val TAG = "MainViewModel"
    }

    fun generateData() {
        GlobalScope.launch(Dispatchers.IO) {
            estateDao.insertAllEstates(FakeEstateApi.getFakeEstateList())
        }

    }

    fun getAll(): Observable<MutableList<Estate>> =
        estateDao.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())




}