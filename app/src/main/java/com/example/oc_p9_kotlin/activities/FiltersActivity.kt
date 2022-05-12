package com.example.oc_p9_kotlin.activities

import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.oc_p9_kotlin.FiltersViewModelFactory
import com.example.oc_p9_kotlin.R
import com.example.oc_p9_kotlin.databinding.FiltersDialogCheckboxBinding
import com.example.oc_p9_kotlin.events.OnUpdateListEvent
import com.example.oc_p9_kotlin.utils.Utils
import com.example.oc_p9_kotlin.view_models.FiltersViewModel
import io.reactivex.rxjava3.kotlin.addTo
import org.greenrobot.eventbus.EventBus
import java.text.DecimalFormat
import java.text.NumberFormat

class FiltersActivity : CompositeDisposableActivity() {

    companion object {
        private const val TAG = "FiltersActivity"
    }

    private var onUpdateListEvent = OnUpdateListEvent()
    private lateinit var binding: FiltersDialogCheckboxBinding

    private lateinit var viewModel: FiltersViewModel

    private lateinit var filterType: String

    private var filterPriceMin: Int = 0
    private var filterPriceMax: Int = 0

    private var filterSurfaceMin: Int = 0
    private var filterSurfaceMax: Int = 0

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

        //Default filter checked
        binding.filtersTypeHouse.isChecked = true
        filterType = binding.filtersTypeHouse.tag.toString()


        initViewModels()
        initRangeSliders()
        initListeners()
        setContentView(binding.root)

    }

    private fun initViewModels() {

        viewModel =
            ViewModelProvider(this, FiltersViewModelFactory(this)).get(FiltersViewModel::class.java)

    }

    private fun getFilteredList(
        estateType: String,
        priceMin: Int,
        priceMax: Int,
        surfaceMin: Int,
        surfaceMax: Int
    ) {

        bag.clear()

        viewModel.getWithFilters(estateType, priceMin, priceMax, surfaceMin, surfaceMax)
            .subscribe(
                {
                    if (!it.isNullOrEmpty()) {
                        Log.d(TAG, "list received : " + it.toString())

                        //Send eventBus with our new list
                        onUpdateListEvent.setFilteredEstateList(it)
                        EventBus.getDefault().postSticky(onUpdateListEvent)


                    } else {
                        Log.d(TAG, "emptyList")
                    }
                }, {
                    Log.d(TAG, "error getFilteredList " + it.message)
                    Toast.makeText(this, R.string.data_error, Toast.LENGTH_LONG).show()

                }).addTo(bag)
    }


    private fun updateSliderValues() {
        val formatter: NumberFormat = DecimalFormat("#,###")

        binding.filtersPriceMin.text = getString(
            R.string.filters_price_dollars,
            formatter.format(binding.filtersSliderPrice.values[0])
        )
        binding.filtersPriceMax.text = getString(
            R.string.filters_price_dollars,
            formatter.format(binding.filtersSliderPrice.values[1])
        )

        binding.filtersSurfaceMin.text =
            getString(R.string.filters_surface, binding.filtersSliderSurface.values[0].toInt())
        binding.filtersSurfaceMax.text =
            getString(R.string.filters_surface, binding.filtersSliderSurface.values[1].toInt())

        binding.filtersRoomsMin.text = binding.filtersSliderRooms.values[0].toInt().toString()
        binding.filtersRoomsMax.text = binding.filtersSliderRooms.values[1].toInt().toString()

        binding.filtersBathroomsMin.text =
            binding.filtersSliderBathrooms.values[0].toInt().toString()
        binding.filtersBathroomsMax.text =
            binding.filtersSliderBathrooms.values[1].toInt().toString()

        binding.filtersBedroomsMin.text = binding.filtersSliderBedrooms.values[0].toInt().toString()
        binding.filtersBedroomsMax.text = binding.filtersSliderBedrooms.values[1].toInt().toString()

        binding.filtersPhotosMin.text = binding.filtersSliderPhotos.values[0].toInt().toString()
        binding.filtersPhotosMax.text = binding.filtersSliderPhotos.values[1].toInt().toString()

        binding.filtersEntryDateMin.text =
            binding.filtersSliderEntryDate.values[0].toInt().toString()
        binding.filtersEntryDateMax.text =
            binding.filtersSliderEntryDate.values[1].toInt().toString()

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

        for (rangeSlider in rangeSliders) {
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

        filterPriceMin = binding.filtersSliderPrice.values[0].toInt()
        filterPriceMax = binding.filtersSliderPrice.values[1].toInt()

        filterSurfaceMin = binding.filtersSliderSurface.values[0].toInt()
        filterSurfaceMax = binding.filtersSliderSurface.values[1].toInt()

        filterRoomsMin = binding.filtersSliderRooms.values[0].toInt()
        filterRoomsMax = binding.filtersSliderRooms.values[1].toInt()

        filterBathroomsMin = binding.filtersSliderBathrooms.values[0].toInt()
        filterBathroomsMax = binding.filtersSliderBathrooms.values[1].toInt()

        filterBedroomsMin = binding.filtersSliderBedrooms.values[0].toInt()
        filterBedroomsMax = binding.filtersSliderBedrooms.values[1].toInt()

        filterPhotosMin = binding.filtersSliderPhotos.values[0].toInt()
        filterPhotosMax = binding.filtersSliderPhotos.values[1].toInt()

        filterEntryDateMin = binding.filtersSliderEntryDate.values[0].toInt()
        filterEntryDateMax = binding.filtersSliderEntryDate.values[1].toInt()

        filterSaleDateMin = binding.filtersSliderSaleDate.values[0].toInt()
        filterSaleDateMax = binding.filtersSliderSaleDate.values[1].toInt()


        Log.d(
            TAG,
            "filter request with $filterType $filterPriceMin $filterPriceMax $filterSurfaceMin $filterSurfaceMax"
        )

        getFilteredList(
            filterType,
            filterPriceMin,
            filterPriceMax,
            filterSurfaceMin,
            filterSurfaceMax
        )


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