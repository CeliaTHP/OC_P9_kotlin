package com.example.oc_p9_kotlin.view_models

import androidx.lifecycle.ViewModel
import com.example.oc_p9_kotlin.daos.EstateDao
import com.example.oc_p9_kotlin.models.Estate
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddEstateViewModel(val estateDao: EstateDao) : ViewModel() {


    companion object {
        private const val TAG = "AddEstateViewModel"
    }

    fun insertEstate(estate: Estate) : Completable {
           return estateDao.insertEstate(estate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    }

    fun updateEstate(estate: Estate) : Completable {
          return  estateDao.updateEstate(estate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    }

}