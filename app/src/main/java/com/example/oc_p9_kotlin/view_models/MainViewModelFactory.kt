package com.example.oc_p9_kotlin.view_models

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.oc_p9_kotlin.databases.EstateDatabase
import com.example.oc_p9_kotlin.utils.MySchedulers
import java.lang.IllegalArgumentException

class MainViewModelFactory(context: Context): ViewModelProvider.Factory {
    private var estateDao = EstateDatabase.getDatabase(context).estateDao()
    private var mySchedulers = MySchedulers()

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if(modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(estateDao,mySchedulers) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}