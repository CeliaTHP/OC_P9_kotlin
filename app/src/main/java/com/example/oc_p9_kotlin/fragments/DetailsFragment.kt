package com.example.oc_p9_kotlin.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Parcelable
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.oc_p9_kotlin.R
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
import com.example.oc_p9_kotlin.view_models.DetailsViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.osmdroid.bonuspack.location.NominatimPOIProvider
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.FolderOverlay
import org.osmdroid.views.overlay.Marker
import java.io.Serializable
import java.text.DateFormat


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
    private lateinit var viewModel: DetailsViewModel
    private var _binding: FragmentDetailsBinding? = null

    private lateinit var playerFullscreenBinding: ExoPlayerFullscreenBinding


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

        playerFullscreenBinding = ExoPlayerFullscreenBinding.inflate(inflater, container, false)


        return binding.root


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModels()
        initListeners()

    }

    private fun initViewModels() {

        viewModel =
            ViewModelProvider(this).get(DetailsViewModel::class.java)

    }

    private fun initExoPlayer(estate: Estate) {

        player = ExoPlayer.Builder(binding.root.context).build()
        binding.detailsPlayerView.player = player


        if (estate.videos.isNullOrEmpty())
            return

        binding.detailsVideoTitle.visibility = View.VISIBLE
        binding.detailsPlayerView.visibility = View.VISIBLE

        /*
        var videoUri =
            Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4")

         */


        for (video in estate.videos!!) {
            val mediaItem = MediaItem.fromUri(video.uri)
            player?.addMediaItem(mediaItem)
        }


        player?.prepare()

        player?.play()


        playerFullscreenBinding.exoPlay.setOnClickListener {
            Log.d(TAG, "onClick Play")

        }



/*
        playerFullscreenBinding.exoFullscreenIcon.setOnClickListener {
            Log.d(TAG, "onClick FullScreenIcon")

        }


 */

        /*
        binding.detailsPlayerView.setOnClickListener {

            if (fullscreenFlag) {
                //When flag is true
                //Set enter full screen image
                playerViewBinding.exoFullscreenIcon.setImageDrawable(
                    AppCompatResources.getDrawable(
                        binding.root.context,
                        R.drawable.ic_fullscreen
                    )
                )
                activity?.requestedOrientation = (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                fullscreenFlag = false

            } else {
                playerViewBinding.exoFullscreenIcon.setImageDrawable(
                    AppCompatResources.getDrawable(
                        binding.root.context,
                        R.drawable.ic_fullscreen_exit
                    )
                )

                activity?.requestedOrientation = (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
                fullscreenFlag = true
            }

        }


         */


    }

/*
    private fun initExoPlayer() {

        var trackSelector: TrackSelector = DefaultTrackSelector(binding.root.context)

        var videoUri = Uri.parse("https://www.youtube.com/watch?v=RPByhN4Fu_s")
        var mediaItem = MediaItem.fromUri(videoUri)

        //Initialize data source factory
        var dataSourceFactory: HttpDataSource.Factory =
            DefaultHttpDataSource.Factory().setUserAgent("exoplayer_video")


        var mediaSource = ProgressiveMediaSource.Factory(
            dataSourceFactory,
            DefaultExtractorsFactory())



        simpleExoPlayer = ExoPlayer.Builder(binding.root.context)
            .setSeekForwardIncrementMs(10000)
            .setSeekBackIncrementMs(10000)
            .setMediaSourceFactory(mediaSource)
            .setTrackSelector(trackSelector)
            .build()


        binding.detailsPlayerView.player = simpleExoPlayer

        simpleExoPlayer.setMediaItem(mediaItem)
        simpleExoPlayer.prepare()
        simpleExoPlayer.playWhenReady = true

        simpleExoPlayer.addListener(object : Player.Listener {

            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)

                if (playbackState == Player.STATE_BUFFERING) {
                    //When buffering
                    //Show progress bar
                    binding.progressBar.visibility = View.VISIBLE
                } else if (playbackState == Player.STATE_READY) {
                    //When ready
                    //Hide progress bar
                    binding.progressBar.visibility = View.GONE

                }
            }


        })

        playerViewBinding.btnFullscreen.setOnClickListener {

            if (fullscreenFlag) {
                //When flag is true
                //Set enter full screen image
                playerViewBinding.btnFullscreen.setImageDrawable(
                    AppCompatResources.getDrawable(
                        binding.root.context,
                        R.drawable.ic_fullscreen
                    )
                )
                activity?.requestedOrientation = (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                fullscreenFlag = false

            } else {
                playerViewBinding.btnFullscreen.setImageDrawable(
                    AppCompatResources.getDrawable(
                        binding.root.context,
                        R.drawable.ic_fullscreen_exit
                    )
                )

                activity?.requestedOrientation = (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

                fullscreenFlag = true
            }

        }


        simpleExoPlayer.play()


    }


 */


    private fun initListeners() {

        Log.d(TAG, "initListener")
        binding.detailsRefreshButton.setOnClickListener {
            estate?.let {
                Log.d(TAG, "refresh button click listener")
                updateUI(it)
            }
        }

        binding.detailsPhotosFullscreen.setOnClickListener {
            Log.d(TAG, " onClickFullscreen photos")
            estate?.medias?.let {
                Log.d(TAG, "photos not empty")
                viewFullscreenPhotos(it)
            }
        }

            binding.detailsVideosFullscreen.setOnClickListener {
                Log.d(TAG, "onClickFullscreen videos")
                estate?.videos?.let {
                    Log.d(TAG, "videos not empty")
                    viewFullscreenVideos(it)
                }


            }

            binding.detailsMapFullscreen.setOnClickListener {
                Log.d(TAG, " onClickFullscreen map")
                estate?.let {
                        estate -> viewFullscreenMap(estate)
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


        mapView = binding.detailsMapView


        if (InternetUtils.isNetworkAvailable(activity)) {
            Log.d(TAG, "internet is Available")
            binding.detailsMapView.visibility = View.VISIBLE
            binding.detailsMapFullscreen.visibility = View.VISIBLE
            binding.detailsConnectionErrorText.visibility = View.GONE
            binding.detailsRefreshButton.visibility = View.GONE
        } else {
            binding.detailsMapView.visibility = View.GONE
            binding.detailsMapFullscreen.visibility = View.GONE
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
                    binding.root.context,
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

                    poiMarkers.add(poiMarker)
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
            Log.d(TAG, "register EventBus")
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
            Log.d(TAG, "onPause unregister EventBus")
        }
    }

    override fun onStart() {
        super.onStart()
        /*
        if (!EventBus.getDefault().isRegistered(this)) {
            Log.d(TAG, "register EventBus")
            EventBus.getDefault().register(this)
        }

         */

    }


    override fun onStop() {
        super.onStop()

        /*
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
            Log.d(TAG, "unregister EventBus")
        }

         */
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
                    DateFormat.getDateInstance(DateFormat.SHORT).format(estate.entryDate)
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
                        DateFormat.getDateInstance(DateFormat.SHORT)
                            .format(it)
                    )
                }


            } else {
                detailsSold.visibility = View.INVISIBLE
            }

        }
        initExoPlayer(estate)
        initPics(estate)
        initMap(estate)


    }

    private fun initPics(estate: Estate) {

        if (!estate.videos.isNullOrEmpty()) {
            estate.videos.let {

                binding.detailsVideosFullscreen.visibility = View.VISIBLE

            }

        }
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
        }


    }

    private fun viewFullscreenPhotos(medias: List<Media>) {
        Log.d(TAG, "start Full Screen Photos Activity")
        val intent =
            Intent(binding.root.context, FullScreenPictureActivity::class.java)
        intent.putExtra("medias", medias as Serializable)
        startActivity(intent)
    }

    private fun viewFullscreenVideos(medias: List<Media>) {
        Log.d(TAG, "start Full Screen Video Activity")
        val intent =
            Intent(binding.root.context, FullScreenVideoActivity::class.java)
        intent.putExtra("medias", medias as Serializable)
        startActivity(intent)
    }

    private fun viewFullscreenMap(selectedEstate: Estate) {
        Log.d(TAG, "start Full Screen Activity")
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


