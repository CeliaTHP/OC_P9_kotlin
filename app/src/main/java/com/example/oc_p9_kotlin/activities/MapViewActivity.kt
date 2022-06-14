package com.example.oc_p9_kotlin.activities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
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

    companion object {
        private const val LOCATION_REQUEST_CODE = 0
        private const val TAG = "MapViewActivity"
    }

    private lateinit var binding: ActivityMapViewBinding

    private var mapView: MapView? = null
    private lateinit var viewModel: MainViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding = ActivityMapViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModels()
        initListeners()

        initMap()

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
                        if (ActivityCompat.checkSelfPermission(
                                this,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                this,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
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
                    }
                }
            }
        }
    }

    private fun initViewModels() {
        viewModel =
            ViewModelProvider(this, MainViewModelFactory(this)).get(MainViewModel::class.java)
    }


    private fun initListeners() {

        binding.mapViewBackButton.setOnClickListener {
            finish()
        }

        binding.mapViewErrorRefresh.setOnClickListener {
            initMap()
        }
    }

    private fun initMap() {

        initLocationInfo()

        mapView = binding.mapViewMap

        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (InternetUtils.createInstance(connectivityManager).isNetworkAvailable()) {
            binding.mapViewMap.visibility = View.VISIBLE
            binding.mapViewErrorText.visibility = View.GONE
            binding.mapViewErrorRefresh.visibility = View.GONE

        } else {
            binding.mapViewMap.visibility = View.GONE
            binding.mapViewErrorRefresh.visibility = View.VISIBLE
            binding.mapViewErrorText.visibility = View.VISIBLE
        }

        mapView?.setTileSource(TileSourceFactory.MAPNIK)
        mapView?.controller?.setZoom(6.0)

        initEstates()
    }

    private fun initEstates() {
        bag.clear()

        viewModel.getAll().subscribe({
            if (!it.isNullOrEmpty()) {
                initMarkers(it)
            }
        }, {
            Toast.makeText(this, R.string.data_error, Toast.LENGTH_LONG).show()
        }).addTo(bag)

    }

    private fun initMarkers(estateList: MutableList<Estate>) {

        val estateMarkers = FolderOverlay()
        mapView?.overlays?.add(estateMarkers)

        for (estate in estateList) {
            val estateMarker = Marker(mapView)
            estateMarker.title = getString(estate.type.stringValue)
            estateMarker.id = estate.id
            estateMarker.snippet = estate.description
            estateMarker.position =
                GeoPoint(estate.location.latitude, estate.location.longitude)

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
        val userGeoPoint = GeoPoint(location.latitude, location.longitude)
        userMarker.title = getString(R.string.map_user_location)
        userMarker.position = userGeoPoint
        userMarker.icon =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_location_red, null)

        userMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

        mapView?.overlays?.add(userMarker)
        mapView?.controller?.setCenter(userGeoPoint)
        mapView?.controller?.setZoom(10.0)

        mapView?.invalidate()

    }


}