package com.example.oc_p9_kotlin.view_models

import androidx.lifecycle.ViewModel
import com.example.oc_p9_kotlin.daos.EstateDao
import com.example.oc_p9_kotlin.models.Estate
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddEstateViewModel(val estateDao: EstateDao) : ViewModel() {

    private var executor: Executor = Executors.newSingleThreadExecutor()

    companion object {
        private const val TAG = "AddEstateViewModel"
    }

    fun insertEstate(estate: Estate) {
        executor.execute {
            estateDao.insertEstate(estate)

        }
    }

    fun updateEstate(estate: Estate) {
        executor.execute {
            estateDao.updateEstate(estate)
        }
    }

}