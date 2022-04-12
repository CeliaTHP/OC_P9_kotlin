package com.example.oc_p9_kotlin.view_models

import androidx.lifecycle.ViewModel
import com.example.oc_p9_kotlin.daos.EstateDao
import com.example.oc_p9_kotlin.models.Estate
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddEstateViewModel(val estateDao: EstateDao) : ViewModel() {

    companion object {
        private const val TAG = "AddEstateViewModel"
    }

    fun insertEstate(estate: Estate) {
        GlobalScope.launch {
            estateDao.insertEstate(estate)
        }
    }

}