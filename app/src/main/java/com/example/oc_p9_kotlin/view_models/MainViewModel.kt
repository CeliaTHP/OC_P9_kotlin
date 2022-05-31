package com.example.oc_p9_kotlin.view_models

import androidx.lifecycle.ViewModel
import com.example.oc_p9_kotlin.daos.EstateDao
import com.example.oc_p9_kotlin.fakeapi.FakeEstateApi
import com.example.oc_p9_kotlin.models.Estate
import com.example.oc_p9_kotlin.models.EstateType
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainViewModel(val estateDao: EstateDao) : ViewModel() {


    private var executor: Executor = Executors.newSingleThreadExecutor()

    companion object {
        private const val TAG = "MainViewModel"
    }

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

    fun getById(id: String): Observable<Estate> =
        estateDao.getById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


}