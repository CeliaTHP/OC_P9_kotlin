package com.example.oc_p9_kotlin.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oc_p9_kotlin.adapters.ImageAdapter
import com.example.oc_p9_kotlin.adapters.ImageFullScreenAdapter
import com.example.oc_p9_kotlin.databinding.ActivityFullScreenPictureBinding
import com.example.oc_p9_kotlin.fragments.DetailsFragment
import com.example.oc_p9_kotlin.models.Estate
import com.example.oc_p9_kotlin.models.Media
import java.io.Serializable

class FullScreenPictureActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "FullScreenPictureAct"
    }

    private lateinit var binding: ActivityFullScreenPictureBinding

    private lateinit var imageAdapter: ImageFullScreenAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFullScreenPictureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val medias = intent?.getSerializableExtra("medias") as List<Media>

        initListeners()
        initPics(medias)

    }


    private fun initListeners() {

        binding.fullScreenBack.setOnClickListener {
            finish()
        }

    }

    private fun initPics(medias: List<Media>) {

        if (!medias.isNullOrEmpty()) {

            binding.fullScreenDefaultPic.visibility = View.GONE
            binding.fullScreenRecyclerView.visibility = View.VISIBLE

            imageAdapter = ImageFullScreenAdapter(medias)
            binding.fullScreenRecyclerView.adapter = imageAdapter

        }
    }

}