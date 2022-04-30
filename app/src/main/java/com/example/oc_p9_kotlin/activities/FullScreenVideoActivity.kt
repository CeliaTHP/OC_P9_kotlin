package com.example.oc_p9_kotlin.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.oc_p9_kotlin.adapters.ImageFullScreenAdapter
import com.example.oc_p9_kotlin.databinding.ActivityFullScreenPictureBinding
import com.example.oc_p9_kotlin.models.Media

class FullScreenVideoActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "FullScreenVideoAct"
    }

    private lateinit var binding: ActivityFullScreenPictureBinding

    private lateinit var imageAdapter: ImageFullScreenAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // binding = ActivityFullScreenVideoBinding.inflate(layoutInflater)

        //setContentView(binding.root)

        val medias = intent?.getSerializableExtra("medias") as List<Media>

        initListeners()
        initVideos(medias)

    }


    private fun initListeners() {

        binding.fullScreenBack.setOnClickListener {
            finish()
        }

    }

    private fun initVideos(medias: List<Media>) {

        Log.d(TAG, "initVideos")
        if (!medias.isNullOrEmpty()) {

            binding.fullScreenDefaultPic.visibility = View.GONE
            binding.fullScreenRecyclerView.visibility = View.VISIBLE

            imageAdapter = ImageFullScreenAdapter(
                medias,
            )
            binding.fullScreenRecyclerView.adapter = imageAdapter

        }

    }

}