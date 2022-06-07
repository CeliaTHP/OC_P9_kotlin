package com.example.oc_p9_kotlin.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isEmpty
import com.example.oc_p9_kotlin.R
import com.example.oc_p9_kotlin.databinding.ActivityFullScreenMapBinding
import com.example.oc_p9_kotlin.models.Estate
import com.example.oc_p9_kotlin.utils.InternetUtils
import com.example.oc_p9_kotlin.utils.Utils
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.FolderOverlay
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker


class FullScreenMapActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "FullScreenMapAct"
        var marker: Marker? = null
    }

    private lateinit var binding: ActivityFullScreenMapBinding

    private var mapView: MapView? = null
    private var tempMarker: Marker? = null
    private var estate: Estate? = null

    private var hasInternet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFullScreenMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent?.getSerializableExtra("estate") != null)
            estate = intent?.getSerializableExtra("estate") as Estate

        initMap()
        initListeners()

    }


    private fun initListeners() {

        binding.fullScreenBack.setOnClickListener {
            finish()
        }

        binding.detailsRefreshButton.setOnClickListener {
            //Refresh map
            initMap()
        }

        binding.fullscreenMapConfirm.setOnClickListener {

            if (tempMarker == null) {
                if (hasInternet) {
                    Toast.makeText(this, R.string.add_estate_map_no_location, Toast.LENGTH_LONG)
                        .show()
                } else {
                    verifyLocationFields()
                }
            } else {
                marker = tempMarker
                finish()
            }
        }
    }

    private fun verifyLocationFields() {
        var canCreate = true

        //Check if all required fields are filled
        val requiredEditTexts = arrayListOf(
            binding.fullScreenLatitudeInput,
            binding.fullScreenLongitudeInput,
        )

        for (editText in requiredEditTexts) {
            if (editText.editText?.text.toString().toDoubleOrNull() == null) {
                editText.error = getString(R.string.map_location_input_error)
                canCreate = false
            } else {
                editText.error = null
            }
        }

        if (!canCreate)
            return

        if (!(binding.fullScreenLatitudeInput.editText?.text.toString().toDouble() > -90 &&
                    binding.fullScreenLatitudeInput.editText?.text.toString().toDouble() < 90)
        ) {
            canCreate = false
            binding.fullScreenLatitudeInput.editText?.error = getString(R.string.map_latitude_error)
        }

        if (!(binding.fullScreenLongitudeInput.editText?.text.toString().toDouble() > -180 &&
                    binding.fullScreenLongitudeInput.editText?.text.toString().toDouble() < 180)
        ) {
            canCreate = false
            binding.fullScreenLongitudeInput.editText?.error =
                getString(R.string.map_longitude_error)
        }

        if (!canCreate)
            return

        val geoPoint = GeoPoint(
            binding.fullScreenLatitudeInput.editText?.text?.toString()?.toDouble()!!,
            binding.fullScreenLongitudeInput.editText?.text?.toString()?.toDouble()!!
        )
        marker = Marker(mapView)
        marker?.position = geoPoint

        finish()

    }

    private fun initMap() {

        mapView = binding.detailsMapView

        if (InternetUtils.isNetworkAvailable(this)) {
            hasInternet = true
            binding.detailsMapView.visibility = View.VISIBLE
            binding.detailsConnectionErrorText.visibility = View.GONE
            binding.detailsRefreshButton.visibility = View.GONE
            binding.detailsConnectionEnterLocation.visibility = View.GONE
            binding.fullScreenLatitudeInput.visibility = View.GONE
            binding.fullScreenLongitudeInput.visibility = View.GONE

        } else {
            hasInternet = false
            binding.detailsMapView.visibility = View.GONE
            binding.detailsRefreshButton.visibility = View.VISIBLE
            binding.detailsConnectionErrorText.visibility = View.VISIBLE
            binding.detailsConnectionEnterLocation.visibility = View.VISIBLE
            binding.fullScreenLatitudeInput.visibility = View.VISIBLE
            binding.fullScreenLongitudeInput.visibility = View.VISIBLE
        }

        mapView?.setTileSource(TileSourceFactory.MAPNIK)
        mapView?.controller?.setZoom(16.0)

        if (estate != null) {
            binding.fullscreenMapConfirm.visibility = View.GONE
            initMapWithEstate(estate)
        } else {
            binding.fullscreenMapConfirm.visibility = View.VISIBLE
            initMapWithoutEstate()
        }

    }

    private fun initMapWithoutEstate() {
        val startPoint = GeoPoint(48.86025403000657, 2.348531258648622)
        mapView?.controller?.setCenter(startPoint)

        initMapListeners()

    }

    private fun initMapWithEstate(estate: Estate?) {

        if (estate == null)
            return

        val startPoint = GeoPoint(estate.location.latitude, estate.location.longitude)

        val startMarker = Marker(mapView)
        startMarker.setInfoWindow(null)
        startMarker.position = startPoint
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        startMarker.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_location_red, null)

        //Add nearby places marker
        mapView?.overlays?.add(startMarker)
        mapView?.controller?.setCenter(startPoint)

        initPois()
    }

    private fun initMapListeners() {
        //Update marker position on map click
        val eventOverlay = MapEventsOverlay(object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(geoPoint: GeoPoint?): Boolean {
                updateMarker(geoPoint)
                return true
            }

            override fun longPressHelper(geoPoint: GeoPoint?): Boolean {
                updateMarker(geoPoint)
                return true
            }

        })
        mapView?.overlays?.add(eventOverlay)
        mapView?.invalidate()

    }

    private fun updateMarker(geoPoint: GeoPoint?) {

        if (geoPoint == null)
            return

        mapView?.overlays?.remove(tempMarker);
        tempMarker = Marker(mapView)
        tempMarker?.setInfoWindow(null)
        tempMarker?.id = "0"
        tempMarker?.position = geoPoint
        tempMarker?.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        tempMarker?.icon =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_location_green, null)
        mapView?.overlays?.add(tempMarker)
        mapView?.invalidate()
    }

    private fun initPois() {
        val thread = Thread {
            try {
                if (estate?.location?.latitude == null || estate?.location?.longitude == null)
                    return@Thread

                if (mapView?.repository == null)
                    return@Thread

                val pois = estate?.nearbyPlaces

                val poiMarkers = FolderOverlay()
                mapView?.overlays?.add(poiMarkers)

                val poiIcon = AppCompatResources.getDrawable(
                    binding.root.context,
                    R.drawable.ic_location_green
                )

                if (pois != null) {
                    for (poi in pois) {
                        val poiMarker = Marker(mapView)
                        poiMarker.title = poi.name
                        poiMarker.snippet = getString(poi.poiType.stringValue)
                        poiMarker.position = GeoPoint(poi.latitude, poi.longitude)
                        poiMarker.icon = poiIcon
                        val icon = Utils.getIconForPoi(poi.poiType, this)
                        icon?.setTint(ResourcesCompat.getColor(resources, R.color.black, null))
                        poiMarker.image = icon
                        poiMarkers.add(poiMarker)
                    }
                }
                mapView?.invalidate()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        thread.start()
    }
}