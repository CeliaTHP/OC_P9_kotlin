package com.example.oc_p9_kotlin.fragments

import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.oc_p9_kotlin.R
import com.example.oc_p9_kotlin.activities.MainActivity
import com.example.oc_p9_kotlin.databinding.FragmentDetailsBinding
import com.example.oc_p9_kotlin.events.OnEstateEvent
import com.example.oc_p9_kotlin.models.Estate
import com.example.oc_p9_kotlin.utils.InternetUtils
import com.example.oc_p9_kotlin.utils.Utils
import java.text.DecimalFormat
import java.text.NumberFormat
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapListener
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class DetailsFragment : Fragment() {

    companion object {
        private const val TAG: String = "DetailsFragment"
    }

    private var _binding: FragmentDetailsBinding? = null
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1
    private var estate: Estate? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //TODO : uncomment to display map
        Configuration.getInstance()
            .load(context, PreferenceManager.getDefaultSharedPreferences(context));


        _binding = FragmentDetailsBinding.inflate(inflater, container, false)

        return binding.root


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()

    }

    private fun initListeners() {
        binding.detailsRefreshButton.setOnClickListener {
            estate?.let {
                Log.d(TAG, "refresh button click listener")
                updateUI(it)
            }
        }
    }


    @Subscribe(sticky = true)
    fun onEstateEvent(onEstateEvent: OnEstateEvent) {
        estate = onEstateEvent.getSelectedEstate()

        estate?.let {
            Log.d(TAG, "onEstateEvent : " + estate.toString())
            updateUI(it)

        }

    }


    private fun initMap(estate: Estate) {

        if (InternetUtils.isNetworkAvailable(activity)) {
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

        binding.detailsMapView.setTileSource(TileSourceFactory.MAPNIK)
        binding.detailsMapView.controller.setZoom(16.0)
        val startPoint = GeoPoint(estate.location.latitude, estate.location.longitude)

        val startMarker = Marker(binding.detailsMapView)
        startMarker.setInfoWindow(null)
        startMarker.position = startPoint
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        startMarker.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_location_red, null)

        //add nearby places marker
        binding.detailsMapView.overlays.add(startMarker)
        binding.detailsMapView.controller.setCenter(startPoint)


    }


    override fun onResume() {
        super.onResume()
        binding.detailsMapView.onResume()
        if (!EventBus.getDefault().isRegistered(this)) {
            Log.d(TAG, "register EventBus")
            EventBus.getDefault().register(this)
        }

    }

    override fun onPause() {
        super.onPause()
        binding.detailsMapView.onPause()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
            Log.d(TAG, "onPause unregister EventBus")
        }
    }


    private fun updateUI(estate: Estate) {
        Log.d(TAG, "updateUI $estate")

        binding.detailsType.text =
            Estate.getEstateType(context, estate.type)

        binding.detailsPrice.text = Utils().getPrice(estate)
        binding.detailsCity.text = estate.city

        binding.detailsDescriptionText.text = estate.description
        binding.detailsSurface.text =
            getString(R.string.details_surface, estate.surfaceInSquareMeters)
        binding.detailsRooms.text = getString(R.string.details_rooms, estate.rooms.toString())
        binding.detailsBathrooms.text =
            getString(R.string.details_bathrooms, estate.bathrooms.toString())
        binding.detailsBedrooms.text =
            getString(R.string.details_bedrooms, estate.bedrooms.toString())
        binding.detailsAddress.text = estate.address

        if (!estate.isAvailable) {
            binding.detailsSold.visibility = View.VISIBLE
        } else {
            binding.detailsSold.visibility = View.INVISIBLE
        }


        initMap(estate)

    }


    override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this)) {
            Log.d(TAG, "register EventBus")
            EventBus.getDefault().register(this)
        }


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onStop() {
        super.onStop()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
            Log.d(TAG, "unregister EventBus")
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


