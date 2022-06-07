package com.example.oc_p9_kotlin.view_models

import androidx.lifecycle.ViewModel
import com.example.oc_p9_kotlin.daos.EstateDao
import com.example.oc_p9_kotlin.models.Estate
import com.example.oc_p9_kotlin.utils.MySchedulers
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EditEstateViewModel(
    val estateDao: EstateDao,
    private val mySchedulers: MySchedulers
) : ViewModel() {


    companion object {
        private const val TAG = "EditEstateViewModel"
    }

    fun insertEstate(estate: Estate): Completable {
        return estateDao.insertEstate(estate)
            .subscribeOn(mySchedulers.io)
            .observeOn(mySchedulers.main)

    }

    fun updateEstate(estate: Estate): Completable {
        return estateDao.updateEstate(estate)
            .subscribeOn(mySchedulers.io)
            .observeOn(mySchedulers.main)

    }

}