package com.example.oc_p9_kotlin.activities

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.oc_p9_kotlin.adapters.ImageFullScreenAdapter
import com.example.oc_p9_kotlin.databinding.ActivityFullScreenPictureBinding
import com.example.oc_p9_kotlin.databinding.ActivityFullScreenVideoBinding
import com.example.oc_p9_kotlin.fragments.DetailsFragment
import com.example.oc_p9_kotlin.models.Estate
import com.example.oc_p9_kotlin.models.Media
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.Player

class FullScreenVideoActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "FullScreenVideoAct"
    }

    private lateinit var binding: ActivityFullScreenVideoBinding

    private var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFullScreenVideoBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val medias = intent?.getSerializableExtra("medias") as List<Media>

        initListeners()
        initExoPlayer(medias)

    }

    override fun onResume() {
        super.onResume()

        player?.playWhenReady = true
        player?.playbackState

    }

    override fun onPause() {
        super.onPause()

        player?.playWhenReady = false
        player?.stop()

    }

    private fun initListeners() {
        binding.fullScreenBack.setOnClickListener { finish() }
    }

    private fun initExoPlayer(medias: List<Media>) {

        player = ExoPlayer.Builder(binding.root.context).build()
        binding.fullscreenPlayerView.player = player

        if (medias.isNullOrEmpty())
            return

        requestedOrientation = (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
        binding.fullscreenPlayerView.visibility = View.VISIBLE

        for (video in medias) {
            val mediaItem = MediaItem.fromUri(video.uri)
            player?.addMediaItem(mediaItem)

        }

        player?.prepare()
        player?.play()

    }

}