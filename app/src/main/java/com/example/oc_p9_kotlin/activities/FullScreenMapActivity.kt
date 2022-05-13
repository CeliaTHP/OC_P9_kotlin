package com.example.oc_p9_kotlin.activities

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.oc_p9_kotlin.R
import com.example.oc_p9_kotlin.adapters.ImageAdapter
import com.example.oc_p9_kotlin.adapters.ImageFullScreenAdapter
import com.example.oc_p9_kotlin.databinding.ActivityAddEstateBinding
import com.example.oc_p9_kotlin.databinding.ActivityFullScreenMapBinding
import com.example.oc_p9_kotlin.databinding.ActivityFullScreenPictureBinding
import com.example.oc_p9_kotlin.fragments.DetailsFragment
import com.example.oc_p9_kotlin.models.Estate
import com.example.oc_p9_kotlin.models.Media
import com.example.oc_p9_kotlin.utils.InternetUtils
import org.osmdroid.bonuspack.location.NominatimPOIProvider
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.FolderOverlay
import org.osmdroid.views.overlay.Marker
import java.io.Serializable

class FullScreenMapActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "FullScreenMapAct"
    }

    private lateinit var binding: ActivityFullScreenMapBinding
    private var mapView: MapView? = null

    private var estate: Estate? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFullScreenMapBinding.inflate(layoutInflater)


        setContentView(binding.root)

        estate = intent?.getSerializableExtra("estate") as Estate

        initListeners()

        estate?.let {
            Log.d(TAG, it.toString())

            initMap(it)

        }

    }



    private fun initListeners() {

        binding.fullScreenBack.setOnClickListener {
            finish()
        }


        binding.detailsRefreshButton.setOnClickListener {
            estate?.let {
                initMap(it)
            }
        }

    }

    private fun initMap(estate: Estate) {

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

    private fun initPois() {
        val thread = Thread {
            try {

                if (estate?.location?.latitude == null || estate?.location?.longitude == null)
                    return@Thread
                val geoPoint = GeoPoint(estate?.location?.latitude!!, estate?.location?.longitude!!)

                if (mapView?.repository == null)
                    return@Thread

                val poiProvider = NominatimPOIProvider("OSMBonusPackTutoUserAgent")

                val pois = poiProvider.getPOICloseTo(
                    GeoPoint(
                        geoPoint
                    ), "cinema", 5, 0.1
                )

                val poiMarkers = FolderOverlay()

                mapView?.overlays?.add(poiMarkers)

                Log.d(TAG, pois.toString())

                val poiIcon = AppCompatResources.getDrawable(
                    this,
                    R.drawable.ic_location_green
                )
                for (poi in pois) {
                    val poiMarker = Marker(mapView)
                    poiMarker.title = poi.mType
                    if (poi.mDescription != null)
                        poiMarker.snippet = poi.mDescription

                    poiMarker.position = poi.mLocation
                    poiMarker.icon = poiIcon
                    if (poi.mThumbnail != null) {


                        val bitmap: Bitmap = Glide
                            .with(this)
                            .asBitmap()
                            .load(poi.mThumbnailPath)
                            .submit(100, 100)
                            .get()


                        poiMarker.image = BitmapDrawable(binding.root.resources, bitmap)

                        //poiMarker.image = BitmapDrawable(binding.root.resources, poi.thumbnail).

                    } else {
                        poiMarker.image = null
                    }

                    poiMarkers.add(poiMarker)
                }
                mapView?.invalidate()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        thread.start()


    }
}