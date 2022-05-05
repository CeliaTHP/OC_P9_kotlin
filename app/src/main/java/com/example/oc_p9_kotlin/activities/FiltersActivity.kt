package com.example.oc_p9_kotlin.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.oc_p9_kotlin.adapters.ImageFullScreenAdapter
import com.example.oc_p9_kotlin.databinding.ActivityFullScreenPictureBinding
import com.example.oc_p9_kotlin.databinding.FiltersDialogBinding
import com.example.oc_p9_kotlin.databinding.FiltersDialogCheckboxBinding
import com.example.oc_p9_kotlin.databinding.FiltersDialogNewBinding
import com.example.oc_p9_kotlin.models.Media
import com.example.oc_p9_kotlin.utils.Utils
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.slider.LabelFormatter
import com.google.android.material.slider.RangeSlider
import java.text.DecimalFormat
import java.text.NumberFormat

class FiltersActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "FiltersActivity"
    }

    private lateinit var binding: FiltersDialogCheckboxBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FiltersDialogCheckboxBinding.inflate(layoutInflater)

        initRangeSliders()
        initListeners()
        setContentView(binding.root)

    }

    private fun initRangeSliders() {

        //TODO : handle currency converter
        binding.filtersSliderPrice.setValues(0f,500000f)

        Utils().initSlider(binding.filtersSliderPrice,"$")


        binding.filtersSliderSurface.setValues(10f,20f)

        Utils().initSlider(binding.filtersSliderSurface,"m²")


        binding.filtersSliderRooms.setValues(3f,6f)
        Utils().initSlider(binding.filtersSliderRooms)


        binding.filtersSliderBathrooms.setValues(2f,4f)
        Utils().initSlider(binding.filtersSliderBathrooms)


        binding.filtersSliderBedrooms.setValues(2f,6f)
        Utils().initSlider(binding.filtersSliderBedrooms)

        binding.filtersSliderPhotos.setValues(3f,5f)
        Utils().initSlider(binding.filtersSliderPhotos)

        binding.filtersSliderEntryDate.setValues(0f,10f)
        Utils().initSlider(binding.filtersSliderEntryDate)

        binding.filtersSliderSaleDate.setValues(0f,10f)
        Utils().initSlider(binding.filtersSliderSaleDate)








       // binding.filtersSliderPrice.addOnChangeListener { slider, value, fromUser ->  }

    }


    private fun initListeners() {

        binding.filtersConfirmButton.setOnClickListener {
            Log.d(TAG, "onConfirm")
            finish()
        }


    }

}