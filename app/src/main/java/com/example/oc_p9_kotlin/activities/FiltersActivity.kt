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
        binding.filtersSliderPrice.labelBehavior = LabelFormatter.LABEL_FLOATING
        binding.filtersSliderPrice.setValues(0f,500000f)

        binding.filtersSliderPrice.setLabelFormatter(object: LabelFormatter{
            override fun getFormattedValue(value: Float): String {
                val formatter: NumberFormat = DecimalFormat("#,###")

                return formatter.format(value) + " $"
            }

        })

        binding.filtersSliderPrice.addOnChangeListener { slider, value, fromUser ->  }

    }


    private fun initListeners() {

        binding.filtersConfirmButton.setOnClickListener {
            Log.d(TAG, "onConfirm")
            finish()
        }


    }

}