package com.example.oc_p9_kotlin

import com.example.oc_p9_kotlin.models.Estate

class OnEstateEvent {

    private lateinit var estate: Estate

    fun setSelectedEstate(estate: Estate){
        this.estate = estate
    }

    fun getSelectedEstate(): Estate {
        return estate
    }

}