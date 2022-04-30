package com.example.oc_p9_kotlin.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.oc_p9_kotlin.adapters.ImageFullScreenAdapter
import com.example.oc_p9_kotlin.databinding.ActivityFullScreenPictureBinding
import com.example.oc_p9_kotlin.databinding.FiltersDialogBinding
import com.example.oc_p9_kotlin.models.Media

class FiltersActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "FiltersActivity"
    }

    private lateinit var binding:FiltersDialogBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

         binding = FiltersDialogBinding.inflate(layoutInflater)

        initListeners()
        setContentView(binding.root)

    }


    private fun initListeners() {

        binding.filtersConfirmButton.setOnClickListener {
            Log.d(TAG, "onConfirm")
            finish()
        }


    }

}