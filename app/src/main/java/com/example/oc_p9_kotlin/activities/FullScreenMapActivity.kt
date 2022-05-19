package com.example.oc_p9_kotlin.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
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
    }

    private lateinit var binding: ActivityFullScreenMapBinding
    private var mapView: MapView? = null

    private var estate: Estate? = null
    private var marker: Marker? = null


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
        if (estate == null) {

            Log.d(TAG, "estate null")

        } else {
            Log.d(TAG, "estate  : $estate")
        }

        binding.fullScreenBack.setOnClickListener {
            finish()
        }


        binding.detailsRefreshButton.setOnClickListener {

            initMap()

        }

    }

    private fun initMap() {

        Log.d(TAG, "initMap")

        mapView = binding.detailsMapView

        if (InternetUtils.isNetworkAvailable(this)) {
            Log.d(TAG, "internet is Available")
            binding.detailsMapView.visibility = View.VISIBLE
            binding.detailsConnectionErrorText.visibility = View.GONE
            binding.detailsRefreshButton.visibility = View.GONE
        } else {
            binding.detailsMapView.visibility = View.GONE
            binding.detailsRefreshButton.visibility = View.VISIBLE
            binding.detailsConnectionErrorText.visibility = View.VISIBLE
            Log.d(TAG, "internet is not Available")

        }

        mapView?.setTileSource(TileSourceFactory.MAPNIK)
        mapView?.controller?.setZoom(16.0)

        if (estate != null) {
            Log.d(TAG, estate.toString())
            binding.fullscreenMapConfirm.visibility = View.GONE
            initMapWithEstate(estate)
        } else {
            binding.fullscreenMapConfirm.visibility = View.VISIBLE
            initMapWithoutEstate()
        }

    }

    private fun initMapWithoutEstate() {

        val startPoint = GeoPoint(46.7115817262967, 2.540951984635548)
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

        //add nearby places marker
        mapView?.overlays?.add(startMarker)
        mapView?.controller?.setCenter(startPoint)

        initPois()
    }

    private fun initMapListeners() {

        val eventOverlay = MapEventsOverlay(object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(geoPoint: GeoPoint?): Boolean {
                Log.d(TAG, "singleTapConfirmed : $geoPoint")
                updateMarker(geoPoint)

                return true
            }

            override fun longPressHelper(geoPoint: GeoPoint?): Boolean {
                Log.d(TAG, "longPress : $geoPoint")
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

        mapView?.overlays?.remove(marker);

        marker = Marker(mapView)

        marker?.setInfoWindow(null)
        marker?.id = "0"
        marker?.position = geoPoint
        marker?.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker?.icon =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_location_green, null)

        mapView?.overlays?.add(marker)
        mapView?.invalidate()

    }



    private fun initPois() {
        val thread = Thread {
            try {

                if (estate?.location?.latitude == null || estate?.location?.longitude == null)
                    return@Thread
                val geoPoint = GeoPoint(estate?.location?.latitude!!, estate?.location?.longitude!!)

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
                        Log.d(TAG, " POI : $poi")
                        val poiMarker = Marker(mapView)
                        poiMarker.title = poi.name
                        // if (poi.mDescription != null)
                        //   poiMarker.snippet = poi.mDescription
                        poiMarker.snippet = getString(poi.poiType.stringValue)
                        poiMarker.position = GeoPoint(poi.latitude, poi.longitude)
                        poiMarker.icon = poiIcon
                        var icon = Utils.getIconForPoi(poi.poiType, this)
                        icon?.setTint(ResourcesCompat.getColor(resources, R.color.black, null))
                        poiMarker.image = icon
                        /*
                                if (poi.mThumbnail != null) {

                                    val bitmap: Bitmap = Glide
                                        .with(binding.root.context)
                                        .asBitmap()
                                        .load(poi.mThumbnailPath)
                                        .submit(100, 100)
                                        .get()

                                    poiMarker.image = BitmapDrawable(binding.root.resources, bitmap)

                                    //poiMarker.image = BitmapDrawable(binding.root.resources, poi.thumbnail).

                                } else {
                                    poiMarker.image = null
                                }
                                 */

                        Log.d(TAG, "marker : " + poiMarker.position.toString())
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