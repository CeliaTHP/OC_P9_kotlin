package com.example.oc_p9_kotlin.events

import com.example.oc_p9_kotlin.models.Estate

class OnUpdateListEvent {

    private lateinit var estateList: List<Estate>

    fun setEstateList(estateList: List<Estate>){
        this.estateList = estateList
    }

    fun getSelectedEstate(): List<Estate> {
        return estateList
    }
}