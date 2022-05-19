package com.example.oc_p9_kotlin

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.oc_p9_kotlin.daos.EstateDao
import com.example.oc_p9_kotlin.databases.EstateDatabase
import com.example.oc_p9_kotlin.view_models.AddEstateViewModel
import com.example.oc_p9_kotlin.view_models.EditEstateViewModel
import com.example.oc_p9_kotlin.view_models.MainViewModel
import java.lang.IllegalArgumentException

class EditEstateViewModelFactory(context: Context): ViewModelProvider.Factory {
    private var estateDao = EstateDatabase.getDatabase(context).estateDao()

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if(modelClass.isAssignableFrom(EditEstateViewModel::class.java)) {
            return EditEstateViewModel(estateDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}