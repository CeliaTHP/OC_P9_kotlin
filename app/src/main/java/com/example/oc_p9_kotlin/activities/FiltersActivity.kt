package com.example.oc_p9_kotlin.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import com.example.oc_p9_kotlin.R
import com.example.oc_p9_kotlin.databinding.FiltersDialogCheckboxBinding
import com.example.oc_p9_kotlin.models.EstateType
import com.example.oc_p9_kotlin.utils.Utils
import java.text.DecimalFormat
import java.text.NumberFormat

class FiltersActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "FiltersActivity"
    }

    private lateinit var binding: FiltersDialogCheckboxBinding

    private var filterType: String? = null

    private var filterPriceMin: Float? = null
    private var filterPriceMax: Float? = null

    private var filterSurfaceMin: Float? = null
    private var filterSurfaceMax: Float? = null

    private var filterRoomsMin: Int? = null
    private var filterRoomsMax: Int? = null


    private var filterBathroomsMin: Int? = null
    private var filterBathroomsMax: Int? = null

    private var filterBedroomsMin: Int? = null
    private var filterBedroomsMax: Int? = null

    private var filterPhotosMin: Int? = null
    private var filterPhotosMax: Int? = null


    private var filterEntryDateMin: Int? = null
    private var filterEntryDateMax: Int? = null

    private var filterSaleDateMin: Int? = null
    private var filterSaleDateMax: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FiltersDialogCheckboxBinding.inflate(layoutInflater)

        initRangeSliders()
        initListeners()
        setContentView(binding.root)

    }

    private fun updateSliderValues() {
        val formatter: NumberFormat = DecimalFormat("#,###")

        binding.filtersPriceMin.text = getString(R.string.filters_price_dollars,formatter.format(binding.filtersSliderPrice.values[0]))
        binding.filtersPriceMax.text = getString(R.string.filters_price_dollars,formatter.format(binding.filtersSliderPrice.values[1]))

        binding.filtersSurfaceMin.text = getString(R.string.filters_surface,binding.filtersSliderSurface.values[0].toInt())
       binding.filtersSurfaceMax.text = getString(R.string.filters_surface,binding.filtersSliderSurface.values[1].toInt())

        binding.filtersRoomsMin.text = binding.filtersSliderRooms.values[0].toInt().toString()
        binding.filtersRoomsMax.text = binding.filtersSliderRooms.values[1].toInt().toString()

        binding.filtersBathroomsMin.text = binding.filtersSliderBathrooms.values[0].toInt().toString()
        binding.filtersBathroomsMax.text = binding.filtersSliderBathrooms.values[1].toInt().toString()

        binding.filtersBedroomsMin.text = binding.filtersSliderBedrooms.values[0].toInt().toString()
        binding.filtersBedroomsMax.text = binding.filtersSliderBedrooms.values[1].toInt().toString()

        binding.filtersPhotosMin.text = binding.filtersSliderPhotos.values[0].toInt().toString()
        binding.filtersPhotosMax.text = binding.filtersSliderPhotos.values[1].toInt().toString()

        binding.filtersEntryDateMin.text = binding.filtersSliderEntryDate.values[0].toInt().toString()
        binding.filtersEntryDateMax.text = binding.filtersSliderEntryDate.values[1].toInt().toString()

        binding.filtersSaleDateMin.text = binding.filtersSliderSaleDate.values[0].toInt().toString()
        binding.filtersSaleDateMax.text = binding.filtersSliderSaleDate.values[1].toInt().toString()








    }

    private fun initRangeSliders() {


        //TODO : handle currency converter
        binding.filtersSliderPrice.setValues(0f, 500000f)
        Utils().initSlider(binding.filtersSliderPrice, "$")


        binding.filtersSliderSurface.setValues(10f, 20f)
        Utils().initSlider(binding.filtersSliderSurface, "m²")


        binding.filtersSliderRooms.setValues(3f, 6f)
        Utils().initSlider(binding.filtersSliderRooms)


        binding.filtersSliderBathrooms.setValues(2f, 4f)
        Utils().initSlider(binding.filtersSliderBathrooms)


        binding.filtersSliderBedrooms.setValues(2f, 6f)
        Utils().initSlider(binding.filtersSliderBedrooms)

        binding.filtersSliderPhotos.setValues(3f, 5f)
        Utils().initSlider(binding.filtersSliderPhotos)

        binding.filtersSliderEntryDate.setValues(0f, 10f)
        Utils().initSlider(binding.filtersSliderEntryDate)

        binding.filtersSliderSaleDate.setValues(0f, 10f)
        Utils().initSlider(binding.filtersSliderSaleDate)

        updateSliderValues()

    }


    private fun initListeners() {

        var radioButtons = arrayListOf(
            binding.filtersTypeHouse,
            binding.filtersTypeApartment,
            binding.filtersTypeBuilding,
            binding.filtersTypeLoft,
            binding.filtersTypeApartmentCastle,
            binding.filtersTypeBoat,
            binding.filtersTypeMansion,
            binding.filtersTypeSite,
            binding.filtersTypeOther
        )

        var rangeSliders = arrayListOf(
            binding.filtersSliderPrice,
            binding.filtersSliderSurface,
            binding.filtersSliderRooms,
            binding.filtersSliderBathrooms,
            binding.filtersSliderBedrooms,
            binding.filtersSliderPhotos,
            binding.filtersSliderEntryDate,
            binding.filtersSliderSaleDate
        )



        for (radioButton in radioButtons) {
            radioButton.setOnClickListener {
                onRadioButtonClick(radioButton, radioButtons)

            }
        }

        for(rangeSlider in rangeSliders) {
            rangeSlider.addOnChangeListener { slider, value, fromUser ->
                updateSliderValues()
            }
        }

        binding.filtersConfirmButton.setOnClickListener {
            verifyFields()
            Log.d(TAG, "onConfirm")
            finish()
        }


    }

    private fun verifyFields() {
        filterPriceMin = binding.filtersSliderPrice.values[0]
        filterPriceMax = binding.filtersSliderPrice.values[1]

        Log.d(TAG, "$filterPriceMin to $filterPriceMax")


    }

    private fun onRadioButtonClick(
        selectedButton: RadioButton,
        radioButtons: ArrayList<RadioButton>
    ) {

        for (radioButton in radioButtons) {
            radioButton.isChecked = false
        }


        selectedButton.isChecked = true

        filterType = selectedButton.tag.toString()

        Log.d(TAG, "onRadioClick filter : $filterType")

    }


}