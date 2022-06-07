package com.example.oc_p9_kotlin

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.oc_p9_kotlin.databases.EstateDatabase
import com.example.oc_p9_kotlin.utils.MySchedulers
import com.example.oc_p9_kotlin.view_models.EditEstateViewModel
import java.lang.IllegalArgumentException

class EditEstateViewModelFactory(context: Context) : ViewModelProvider.Factory {
    private var estateDao = EstateDatabase.getDatabase(context).estateDao()
    private var mySchedulers = MySchedulers()

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(EditEstateViewModel::class.java)) {
            return EditEstateViewModel(estateDao, mySchedulers) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}