package com.example.oc_p9_kotlin.activities

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.oc_p9_kotlin.MainViewModelFactory
import com.example.oc_p9_kotlin.R
import com.example.oc_p9_kotlin.databinding.ActivityMapViewBinding
import com.example.oc_p9_kotlin.models.Estate
import com.example.oc_p9_kotlin.utils.InternetUtils
import com.example.oc_p9_kotlin.view_models.MainViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import io.reactivex.rxjava3.kotlin.addTo
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.FolderOverlay
import org.osmdroid.views.overlay.Marker


class MapViewActivity : CompositeDisposableActivity(), LocationListener {

    private lateinit var binding: ActivityMapViewBinding
    private var mapView: MapView? = null

    private lateinit var viewModel: MainViewModel

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    companion object {
        private const val LOCATION_REQUEST_CODE = 0
        private const val TAG = "MapViewActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding = ActivityMapViewBinding.inflate(layoutInflater)


        initViewModels()
        initListeners()
        initMap()
        initLocationInfo()

        setContentView(binding.root)

    }

    private fun initLocationInfo() {


        checkAndRequestPermissions()


    }

    override fun onLocationChanged(location: Location) {
        initCurrentLocationMarker(location)
    }

    override fun onLocationChanged(locations: MutableList<Location>) {
        super.onLocationChanged(locations)

    }

    private fun checkAndRequestPermissions() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(TAG, " shouldRequestPerm")
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_REQUEST_CODE
            )
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    Log.d(TAG, "location $location")
                    initCurrentLocationMarker(location)
                }

            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_REQUEST_CODE) {
            for (i in permissions.indices) {
                val permission = permissions[i]
                val grantResult = grantResults[i]
                if (permission == Manifest.permission.ACCESS_COARSE_LOCATION) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "perm granted")

                        if (ActivityCompat.checkSelfPermission(
                                this,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                this,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            Log.d(TAG, "should request Perm")

                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            checkAndRequestPermissions()
                            return
                        }
                    } else {
                        Toast.makeText(this, R.string.map_user_location_error, Toast.LENGTH_LONG)
                            .show()
                        Log.d(TAG, "Refused Perm")
                    }
                }
            }
        }
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
        mapView?.controller?.setZoom(6.0)

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
            estateMarker.position =
                GeoPoint(estate.location.latitude, estate.location.longitude)


            estate.medias?.let {
                Glide.with(this)
                    .load(it[0].uri)
                    .error(R.drawable.ic_house)
                    .placeholder(R.drawable.ic_house)
                //  .into()
            }

            estateMarker.icon =
                ResourcesCompat.getDrawable(resources, R.drawable.ic_location_green, null)

            val image = ResourcesCompat.getDrawable(resources, R.drawable.ic_house, null)
            image?.setTint(ResourcesCompat.getColor(resources, R.color.black, null))
            estateMarker.image = image


            estateMarkers.add(estateMarker)

            mapView?.controller?.setCenter(estateMarker.position)

        }
        mapView?.invalidate()


    }

    private fun initCurrentLocationMarker(location: Location?) {
        if (location == null)
            return

        val userMarker = Marker(mapView)
        var userGeoPoint =GeoPoint(location.latitude, location.longitude)
        userMarker.title = getString(R.string.map_user_location)
        userMarker.position =  userGeoPoint
        userMarker.icon =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_location_red, null)

        userMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

        mapView?.overlays?.add(userMarker)
        mapView?.controller?.setCenter(userGeoPoint)
        mapView?.controller?.setZoom(10.0)


        mapView?.invalidate()


    }


}