package com.example.oc_p9_kotlin.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.oc_p9_kotlin.MainViewModelFactory
import com.example.oc_p9_kotlin.R
import com.example.oc_p9_kotlin.databinding.ActivityMapViewBinding
import com.example.oc_p9_kotlin.models.Estate
import com.example.oc_p9_kotlin.utils.InternetUtils
import com.example.oc_p9_kotlin.view_models.MainViewModel
import io.reactivex.rxjava3.kotlin.addTo
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.FolderOverlay
import org.osmdroid.views.overlay.Marker

class MapViewActivity : CompositeDisposableActivity() {

    private lateinit var binding: ActivityMapViewBinding
    private var mapView: MapView? = null

    private lateinit var viewModel: MainViewModel

    companion object {
        private const val TAG = "MapViewActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapViewBinding.inflate(layoutInflater)


        initViewModels()
        initListeners()
        initMap()

        setContentView(binding.root)

    }

    private fun initViewModels() {

        viewModel =
            ViewModelProvider(this, MainViewModelFactory(this)).get(MainViewModel::class.java)


    }


    fun initListeners() {
        binding.mapViewBackButton.setOnClickListener {
            finish()
        }

        binding.mapViewErrorRefresh.setOnClickListener {
            initMap()
        }
    }

    fun initMap() {

        mapView = binding.mapViewMap

        if (InternetUtils.isNetworkAvailable(this)) {
            Log.d(TAG, "internet is Available")
            binding.mapViewMap.visibility = View.VISIBLE
            binding.mapViewErrorText.visibility = View.GONE
            binding.mapViewErrorRefresh.visibility = View.GONE

        } else {
            binding.mapViewMap.visibility = View.GONE
            binding.mapViewErrorRefresh.visibility = View.VISIBLE
            binding.mapViewErrorText.visibility = View.VISIBLE


            Log.d(TAG, "internet is not Available")

        }

        mapView?.setTileSource(TileSourceFactory.MAPNIK)
        mapView?.controller?.setZoom(10.0)

        initEstates()
    }

    private fun initEstates() {

        viewModel.getAll().subscribe({
            if (it.isNullOrEmpty()) {

            } else {
                Log.d(TAG, "estateList " + it.size)
                initMarkers(it)

            }

        }, {

        }).addTo(bag)

    }

    private fun initMarkers(estateList: MutableList<Estate>) {

        val estateMarkers = FolderOverlay()
        mapView?.overlays?.add(estateMarkers)

        for (estate in estateList) {
            val estateMarker = Marker(mapView)

            estateMarker.title = getString(estate.type.stringValue)
            // if (poi.mDescription != null)
            //   poiMarker.snippet = poi.mDescription
            estateMarker.snippet = estate.description
            estateMarker.position = GeoPoint(estate.location.latitude, estate.location.longitude)
            estateMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

            estate.medias?.let {
                Glide.with(this)
                    .load(it[0].uri)
                    .error(R.drawable.ic_house)
                    .placeholder(R.drawable.ic_house)
                  //  .into()
            }

            estateMarker.icon =
                ResourcesCompat.getDrawable(resources, R.drawable.ic_location_red, null)

            val image = ResourcesCompat.getDrawable(resources, R.drawable.ic_house, null)
            image?.setTint(ResourcesCompat.getColor(resources, R.color.black, null))
            estateMarker.image = image


            estateMarkers.add(estateMarker)

            mapView?.controller?.setCenter(estateMarker.position)

        }
        mapView?.invalidate()





    }


}