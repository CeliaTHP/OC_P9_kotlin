package com.example.oc_p9_kotlin.fragments

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.oc_p9_kotlin.R
import com.example.oc_p9_kotlin.activities.EditEstateActivity
import com.example.oc_p9_kotlin.activities.FullScreenMapActivity
import com.example.oc_p9_kotlin.activities.FullScreenPictureActivity
import com.example.oc_p9_kotlin.activities.FullScreenVideoActivity
import com.example.oc_p9_kotlin.adapters.ImageAdapter
import com.example.oc_p9_kotlin.databinding.ExoPlayerFullscreenBinding
import com.example.oc_p9_kotlin.databinding.FragmentDetailsBinding
import com.example.oc_p9_kotlin.events.OnEstateEvent
import com.example.oc_p9_kotlin.models.Estate
import com.example.oc_p9_kotlin.models.Media
import com.example.oc_p9_kotlin.utils.InternetUtils
import com.example.oc_p9_kotlin.utils.Utils
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.FolderOverlay
import org.osmdroid.views.overlay.Marker
import java.io.Serializable


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class DetailsFragment : Fragment() {

    companion object {
        private const val TAG: String = "DetailsFragment"
        private var player: ExoPlayer? = null

        fun getPlayer(): ExoPlayer? {
            return player
        }

    }

    private lateinit var imageAdapter: ImageAdapter

    private var mapView: MapView? = null
    private var _binding: FragmentDetailsBinding? = null

    private lateinit var playerFullscreenBinding: ExoPlayerFullscreenBinding

    private var estate: Estate? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Configuration.getInstance()
            .load(context, PreferenceManager.getDefaultSharedPreferences(context));

        _binding = FragmentDetailsBinding.inflate(inflater, container, false)

        playerFullscreenBinding = ExoPlayerFullscreenBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()

    }

    private fun initExoPlayer(estate: Estate) {

        if (!estate.videos.isNullOrEmpty()) {
            estate.videos.let {
                binding.detailsVideosFullscreen.visibility = View.VISIBLE
                binding.detailsPlayerView.visibility = View.VISIBLE
            }

        } else {
            binding.detailsPlayerView.visibility = View.GONE
            binding.detailsVideosFullscreen.visibility = View.GONE
            player = ExoPlayer.Builder(binding.root.context).build()
            binding.detailsPlayerView.player = player
        }

        player = ExoPlayer.Builder(binding.root.context).build()
        binding.detailsPlayerView.player = player

        if (estate.videos.isNullOrEmpty())
            return

        binding.detailsVideoTitle.visibility = View.VISIBLE
        binding.detailsPlayerView.visibility = View.VISIBLE

        for (video in estate.videos!!) {
            val mediaItem = MediaItem.fromUri(video.uri)
            player?.addMediaItem(mediaItem)
        }

        player?.prepare()

        player?.play()

    }


    private fun initListeners() {

        binding.detailsRefreshButton.setOnClickListener {
            estate?.let {
                updateUI(it)
            }
        }
        binding.detailsPhotosFullscreen.setOnClickListener {
            estate?.medias?.let {
                viewFullscreenPhotos(it)
            }
        }
        binding.detailsVideosFullscreen.setOnClickListener {
            estate?.videos?.let {
                viewFullscreenVideos(it)
            }

        }
        binding.detailsMapFullscreen.setOnClickListener {
            estate?.let { estate ->
                viewFullscreenMap(estate)
            }
        }
        binding.detailsEditButton.setOnClickListener {
            estate?.let { estate ->
                val intent =
                    Intent(binding.root.context, EditEstateActivity::class.java)
                intent.putExtra("estate", estate as Serializable)
                this.startActivity(intent)
            }
        }
    }

    @Subscribe(sticky = true)
    fun onEstateEvent(onEstateEvent: OnEstateEvent) {
        onEstateEvent.getSelectedEstate().let {
            estate = it
            updateUI(it)
            initMedias(it)
            initExoPlayer(it)
            initMap(it)
            initPois()
        }
    }


    private fun initMap(estate: Estate) {

        mapView = binding.detailsMapView

        if (InternetUtils.isNetworkAvailable(activity)) {
            binding.detailsMapView.visibility = View.VISIBLE
            binding.detailsMapFullscreen.visibility = View.VISIBLE
            binding.detailsConnectionErrorText.visibility = View.GONE
            binding.detailsRefreshButton.visibility = View.GONE
        } else {
            binding.detailsMapView.visibility = View.GONE
            binding.detailsMapFullscreen.visibility = View.GONE
            binding.detailsRefreshButton.visibility = View.VISIBLE
            binding.detailsConnectionErrorText.visibility = View.VISIBLE
        }

        mapView?.setTileSource(TileSourceFactory.MAPNIK)
        mapView?.controller?.setZoom(16.0)
        val startPoint = GeoPoint(estate.location.latitude, estate.location.longitude)

        val startMarker = Marker(mapView)
        startMarker.title = getString(estate.type.stringValue)
        startMarker.position = startPoint
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        startMarker.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_location_red, null)

        //Add nearby places marker
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
                        val icon = Utils.getIconForPoi(poi.poiType, context)
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


    override fun onResume() {
        super.onResume()
        mapView?.onResume()

        player?.playWhenReady = true
        player?.playbackState

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }

    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()

        player?.playWhenReady = false
        player?.stop()

        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    override fun onStart() {
        super.onStart()

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }

    }

    override fun onStop() {
        super.onStop()

        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }

        player?.playWhenReady = false
        player?.stop()

    }


    private fun updateUI(estate: Estate) {

        with(binding) {
            detailsType.text =
                getString(estate.type.stringValue)
            detailsPrice.text = Utils.getPrice(estate)
            detailsCity.text = estate.city
            detailsDescriptionText.text = estate.description
            detailsSurface.text =
                getString(R.string.details_surface, estate.surfaceInSquareMeters)
            detailsRooms.text = getString(R.string.details_rooms, estate.rooms.toString())
            detailsBathrooms.text =
                getString(R.string.details_bathrooms, estate.bathrooms.toString())
            detailsBedrooms.text =
                getString(R.string.details_bedrooms, estate.bedrooms.toString())
            detailsAddress.text = estate.address
            detailsEntryDate.text =
                root.context.getString(
                    R.string.item_estate_entry_date,
                    Utils.getTodayDate(estate.entryDate)
                )

            if (estate.assignedAgentName != null) {
                detailsAgent.visibility = View.VISIBLE
                detailsAgent.text = getString(R.string.item_estate_agent, estate.assignedAgentName)

            } else {
                detailsAgent.visibility = View.GONE
            }

            if (!estate.isAvailable) {
                detailsSold.visibility = View.VISIBLE

                estate.saleDate?.let {
                    detailsSaleDate.visibility = View.VISIBLE
                    detailsSaleDate.text = root.context.getString(
                        R.string.item_estate_sale_date,
                        Utils.getTodayDate(estate.saleDate)
                    )
                }

            } else {
                detailsSold.visibility = View.INVISIBLE
            }
        }
        initExoPlayer(estate)
        initMedias(estate)
        initMap(estate)

    }

    private fun initMedias(estate: Estate) {

        if (!estate.medias.isNullOrEmpty()) {

            estate.medias?.let {
                binding.detailsDefaultPic.visibility = View.GONE
                binding.detailsPhotosFullscreen.visibility = View.VISIBLE

                imageAdapter = ImageAdapter(
                    it.toMutableList(),
                    false, {}) {
                    viewFullscreenPhotos(it)
                }
                binding.detailsPicsRecyclerView.adapter = imageAdapter

            }
        } else {
            imageAdapter = ImageAdapter(
                mutableListOf(),
                false, {}) {
            }

            binding.detailsPicsRecyclerView.adapter = imageAdapter
            binding.detailsDefaultPic.visibility = View.VISIBLE
            binding.detailsPhotosFullscreen.visibility = View.GONE
        }
    }

    private fun viewFullscreenPhotos(medias: List<Media>) {
        val intent =
            Intent(binding.root.context, FullScreenPictureActivity::class.java)
        intent.putExtra("medias", medias as Serializable)
        startActivity(intent)
    }

    private fun viewFullscreenVideos(medias: List<Media>) {
        val intent =
            Intent(binding.root.context, FullScreenVideoActivity::class.java)
        intent.putExtra("medias", medias as Serializable)
        startActivity(intent)
    }

    private fun viewFullscreenMap(selectedEstate: Estate) {
        val intent =
            Intent(binding.root.context, FullScreenMapActivity::class.java)
        intent.putExtra("estate", selectedEstate as Serializable)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}


