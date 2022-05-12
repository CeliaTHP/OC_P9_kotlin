package com.example.oc_p9_kotlin.events

import com.example.oc_p9_kotlin.models.Estate

class OnUpdateListEvent {

    private lateinit var estateList: MutableList<Estate>

    fun setFilteredEstateList(estateList: MutableList<Estate>){
        this.estateList = estateList
    }

    fun getFilteredEstateList(): MutableList<Estate> {
        return estateList
    }
}