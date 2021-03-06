package com.example.oc_p9_kotlin.activities

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.oc_p9_kotlin.view_models.FiltersViewModelFactory
import com.example.oc_p9_kotlin.R
import com.example.oc_p9_kotlin.databinding.FiltersDialogCheckboxBinding
import com.example.oc_p9_kotlin.events.OnUpdateListEvent
import com.example.oc_p9_kotlin.models.Currency
import com.example.oc_p9_kotlin.utils.Utils
import com.example.oc_p9_kotlin.view_models.FiltersViewModel
import io.reactivex.rxjava3.kotlin.addTo
import org.greenrobot.eventbus.EventBus
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale


class FiltersActivity : CompositeDisposableActivity() {

    companion object {
        private const val TAG = "FiltersActivity"
    }

    private var onUpdateListEvent = OnUpdateListEvent()

    private lateinit var binding: FiltersDialogCheckboxBinding

    private lateinit var viewModel: FiltersViewModel

    private lateinit var filterType: String
    private var currency: Currency? = null

    private var filterPriceMin: Int = 0
    private var filterPriceMax: Int = 0

    private var filterSurfaceMin: Int = 0
    private var filterSurfaceMax: Int = 0

    private var filterRoomsMin: Int = 0
    private var filterRoomsMax: Int = 0

    private var filterBathroomsMin: Int = 0
    private var filterBathroomsMax: Int = 0

    private var filterBedroomsMin: Int = 0
    private var filterBedroomsMax: Int = 0

    private var filterPhotosMin: Int = 0
    private var filterPhotosMax: Int = 0

    private var filterCity: String = ""

    private var filterEntryDateMin: Date = Date()
    private var filterSaleDateMin: Date? = Date()


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

    private fun initCurrency(currency: Currency? = null) {
        when (currency) {
            Currency.DOLLAR -> setCurrencyInDollars()
            Currency.EURO -> setCurrencyInEuros()
            null -> setCurrencyFromLocale()
        }
    }

    private fun setCurrencyFromLocale() {
        //Handle currency according to user selection or default language
        if (Locale.getDefault().language == Locale.FRENCH.language) {
            setCurrencyInEuros()
        } else {
            setCurrencyInDollars()
        }
    }

    private fun setCurrencyInDollars() {
        binding.filtersPriceSymbol.setImageDrawable(
            AppCompatResources
                .getDrawable(this, R.drawable.ic_dollar)
        )
        Utils.initSlider(binding.filtersSliderPrice, "$")
        this.currency = Currency.DOLLAR
    }

    private fun setCurrencyInEuros() {
        binding.filtersPriceSymbol.setImageDrawable(
            AppCompatResources
                .getDrawable(this, R.drawable.ic_euro)
        )
        Utils.initSlider(binding.filtersSliderPrice, "???")
        this.currency = Currency.EURO
    }

    private fun getFilteredList(
        estateType: String,
        priceMin: Int,
        priceMax: Int,
        surfaceMin: Int,
        surfaceMax: Int,
        roomsMin: Int,
        roomsMax: Int,
        bathroomsMin: Int,
        bathroomsMax: Int,
        bedroomsMin: Int,
        bedroomsMax: Int,
        photosMin: Int,
        photosMax: Int,
        entryDate: Date,
        saleDate: Date?
    ) {
        bag.clear()
        viewModel.getWithFilters(
            estateType,
            priceMin,
            priceMax,
            surfaceMin,
            surfaceMax,
            roomsMin,
            roomsMax,
            bathroomsMin,
            bathroomsMax,
            bedroomsMin,
            bedroomsMax,
            photosMin,
            photosMax,
            filterCity,
            entryDate,
            saleDate,
            binding.filtersNearStationCheckbox.isChecked,
            binding.filtersNearPubCheckbox.isChecked,
            binding.filtersNearHostelCheckbox.isChecked,
            binding.filtersNearHospitalCheckbox.isChecked,
            binding.filtersNearSchoolCheckbox.isChecked,
            binding.filtersNearParkCheckbox.isChecked,
            binding.filtersNearRestaurantCheckbox.isChecked,
            binding.filtersNearOtherCheckbox.isChecked,

            )
            .subscribe(
                {
                    if (it.isNullOrEmpty()) {
                        Toast.makeText(this, R.string.filters_empty, Toast.LENGTH_LONG).show()
                    }
                    //Send our new list to event bus
                    onUpdateListEvent.setFilteredEstateList(it)
                    EventBus.getDefault().postSticky(onUpdateListEvent)

                }, {
                    Toast.makeText(this, R.string.data_error, Toast.LENGTH_LONG).show()
                }).addTo(bag)
    }

    private fun updateCurrency() {

        val formatter: NumberFormat = DecimalFormat("#,###")

        if (currency == Currency.DOLLAR) {
            binding.filtersPriceMin.text = getString(
                R.string.filters_price_dollars,
                formatter.format(binding.filtersSliderPrice.values[0])
            )
            binding.filtersPriceMax.text = getString(
                R.string.filters_price_dollars,
                formatter.format(binding.filtersSliderPrice.values[1])
            )
        } else {
            binding.filtersPriceMin.text = getString(
                R.string.filters_price_euros,
                formatter.format(binding.filtersSliderPrice.values[0])
            )
            binding.filtersPriceMax.text = getString(
                R.string.filters_price_euros,
                formatter.format(binding.filtersSliderPrice.values[1])
            )

        }
    }

    private fun updateSliderValues() {

        updateCurrency()

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

    }

    private fun initRangeSliders() {

        binding.filtersSliderPrice.setValues(0f, 500000f)
        initCurrency()

        binding.filtersSliderSurface.setValues(20f, 50f)
        Utils.initSlider(binding.filtersSliderSurface, "m??")

        binding.filtersSliderRooms.setValues(2f, 4f)
        Utils.initSlider(binding.filtersSliderRooms)

        binding.filtersSliderBathrooms.setValues(0f, 1f)
        Utils.initSlider(binding.filtersSliderBathrooms)

        binding.filtersSliderBedrooms.setValues(1f, 2f)
        Utils.initSlider(binding.filtersSliderBedrooms)

        binding.filtersSliderPhotos.setValues(3f, 5f)
        Utils.initSlider(binding.filtersSliderPhotos)

        updateSliderValues()
    }


    private fun initListeners() {

        val radioButtons = arrayListOf(
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

        val rangeSliders = arrayListOf(
            binding.filtersSliderPrice,
            binding.filtersSliderSurface,
            binding.filtersSliderRooms,
            binding.filtersSliderBathrooms,
            binding.filtersSliderBedrooms,
            binding.filtersSliderPhotos,
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

        initDatePickers()

        binding.filtersAvailableCheckbox.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                binding.filtersDateSaleTitle.visibility = View.GONE
                binding.filtersDateSalePicker.visibility = View.GONE
            } else {
                binding.filtersDateSaleTitle.visibility = View.VISIBLE
                binding.filtersDateSalePicker.visibility = View.VISIBLE
            }
        }

        binding.filtersCityInputLayout.editText?.addTextChangedListener {
            filterCity = if (it.isNullOrBlank()) {
                ""
            } else {
                it.toString()
            }
        }

        binding.filtersConfirmButton.setOnClickListener {
            //Launch a filter request with criteria
            verifyFields()
            finish()
        }

        binding.filtersPriceSymbol.setOnClickListener {
            if (currency == Currency.DOLLAR) {
                initCurrency(Currency.EURO)

            } else {
                initCurrency(Currency.DOLLAR)
            }
            updateCurrency()
        }
    }

    private fun initDatePickers() {

        val entryDateCalendar: Calendar = GregorianCalendar()
        binding.filtersDateEntryPicker.init(
            entryDateCalendar.get(Calendar.YEAR),
            entryDateCalendar.get(Calendar.MONTH),
            entryDateCalendar.get(Calendar.DAY_OF_MONTH)
        ) { datePicker, year, month, day ->
            entryDateCalendar.set(
                datePicker.year,
                datePicker.month,
                datePicker.dayOfMonth
            )
            filterEntryDateMin = entryDateCalendar.time
        }

        val saleDateCalendar: Calendar = GregorianCalendar()

        binding.filtersDateSalePicker.init(
            saleDateCalendar.get(Calendar.YEAR),
            saleDateCalendar.get(Calendar.MONTH),
            saleDateCalendar.get(Calendar.DAY_OF_MONTH)
        ) { datePicker, year, month, day ->
            saleDateCalendar.set(
                datePicker.year,
                datePicker.month,
                datePicker.dayOfMonth
            )
            filterSaleDateMin = saleDateCalendar.time
        }

    }

    private fun verifyFields() {

        if (currency == Currency.DOLLAR) {
            filterPriceMin =
                Utils.convertDollarToEuro(binding.filtersSliderPrice.values[0].toInt())
            filterPriceMax =
                Utils.convertDollarToEuro(binding.filtersSliderPrice.values[1].toInt())

        } else {
            filterPriceMin = binding.filtersSliderPrice.values[0].toInt()
            filterPriceMax = binding.filtersSliderPrice.values[1].toInt()
        }
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

        if (binding.filtersAvailableCheckbox.isChecked) {
            filterSaleDateMin = null
        }

        getFilteredList(
            filterType,
            filterPriceMin,
            filterPriceMax,
            filterSurfaceMin,
            filterSurfaceMax,
            filterRoomsMin,
            filterRoomsMax,
            filterBathroomsMin,
            filterBathroomsMax,
            filterBedroomsMin,
            filterBedroomsMax,
            filterPhotosMin,
            filterPhotosMax,
            filterEntryDateMin,
            filterSaleDateMin
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
    }

}