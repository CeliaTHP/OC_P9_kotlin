package com.example.oc_p9_kotlin.view_models

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.oc_p9_kotlin.R
import com.example.oc_p9_kotlin.daos.EstateDao
import com.example.oc_p9_kotlin.fragments.DetailsFragment
import com.example.oc_p9_kotlin.models.Estate
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.osmdroid.bonuspack.location.NominatimPOIProvider
import org.osmdroid.bonuspack.location.POI
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.FolderOverlay
import org.osmdroid.views.overlay.Marker

class DetailsViewModel() : ViewModel() {

    private var executor: Executor = Executors.newSingleThreadExecutor()
    var poisLiveData = MutableLiveData<POI>()

    companion object {
        private const val TAG = "DetailsEstateViewModel"
    }




}