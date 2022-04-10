package com.example.oc_p9_kotlin.activities

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.content.res.ResourcesCompat
import com.example.oc_p9_kotlin.R
import com.example.oc_p9_kotlin.databinding.ActivityAddEstateBinding
import com.example.oc_p9_kotlin.models.Estate
import com.example.oc_p9_kotlin.models.EstateType
import com.example.oc_p9_kotlin.utils.Utils
import java.util.Date
import java.util.Locale
import java.util.UUID


class AddEstateActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "AddEstateActivity"
        private const val AUTOCOMPLETE_REQUEST_CODE = 1

    }

    private lateinit var binding: ActivityAddEstateBinding
    private var estateType: EstateType = EstateType.HOUSE
    private var isInDollars: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Setting up view with binding
        binding = ActivityAddEstateBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Default type
        binding.addEstateTypeButton.text = Estate.getEstateType(this, estateType)

        initCurrency()
        initPlaces()
        initToolbar()
        initListeners()
        initSpinner()

    }

    private fun initPlaces() {

    }

    private fun initCurrency(isInDollars: Boolean? = null) {

        if (isInDollars == null) {
            Log.d(TAG, "no option, default currency used")
            val language = Locale.getDefault().language
            if (language == Locale.FRENCH.language) {
                setCurrencyUI(false)
            } else {
                setCurrencyUI(true)
            }
        } else {
            if (isInDollars) {
                Log.d(TAG, "should be Dollars")
                setCurrencyUI(true)
            } else {
                Log.d(TAG, "should be Euros")
                setCurrencyUI(false)
            }
        }


    }

    private fun setCurrencyUI(isInDollars: Boolean) {
        if (isInDollars) {
            binding.addEstatePriceSwitch.setImageDrawable(
                AppCompatResources
                    .getDrawable(this, R.drawable.ic_dollar)
            )
            binding.addEstatePriceInput.hint = getString(R.string.add_estate_price_hint_dollars)

            this.isInDollars = true

        } else {
            binding.addEstatePriceSwitch.setImageDrawable(
                AppCompatResources
                    .getDrawable(this, R.drawable.ic_euro)
            )
            binding.addEstatePriceInput.hint = getString(R.string.add_estate_price_hint_euros)
            this.isInDollars = false
        }

    }


    private fun initToolbar() {
        //Setting up toolbar
        binding.toolbar.overflowIcon =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_filter, null)
        binding.toolbar.navigationIcon =
            AppCompatResources.getDrawable(this, R.drawable.ic_back_arrow)

        setSupportActionBar(binding.toolbar)
    }

    private fun verifyEstateCreation() {
        var canCreate = true

        val requiredEditTexts = arrayListOf(
            binding.addEstateCityInput,
            binding.addEstateAddressInput,
            binding.addEstatePriceInput,
            binding.addEstateSurfaceInput,
            binding.addEstateRoomsInput,
            binding.addEstateBathroomsInput,
            binding.addEstateBedroomsInput,
            binding.addEstateDescriptionInput
        )

        for (editText in requiredEditTexts) {
            if (editText.editText?.text.isNullOrBlank()) {
                editText.error = "Cannot be Empty"
                canCreate = false
            } else {
                editText.error = null
            }
        }

        if (canCreate) {
            Log.d(TAG, "can create Estate")
            with(binding) {
                var location = Location("")
                location.latitude = 49.54454396
                location.longitude = 2.8484848

                var priceInEuros = if (isInDollars)
                    Utils().convertDollarToEuro(
                        addEstatePriceInput.editText?.text.toString().toInt()
                    )
                else
                    addEstatePriceInput.editText?.text.toString().toInt()

                var estate = Estate(
                    UUID.randomUUID().toString(),
                    estateType,
                    addEstateCityInput.editText?.text.toString(),
                    priceInEuros,
                    addEstateSurfaceInput.editText?.text.toString().toInt(),
                    addEstateRoomsInput.editText?.text.toString().toInt(),
                    addEstateBathroomsInput.editText?.text.toString().toInt(),
                    addEstateBedroomsInput.editText?.text.toString().toInt(),
                    addEstateAddressInput.editText?.text.toString(),
                    //TODO : handle location
                    location,
                    addEstateDescriptionInput.editText?.text.toString(),
                    Date(),
                    null,
                    isAvailable = true,
                    false,
                    null
                )
                Log.d(TAG, " created : " + estate.toString())
                //TODO : create estate
            }


        } else {
            Log.d(TAG, "can NOT create Estate")

        }


    }

    private fun initListeners() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.fab.setOnClickListener {
            verifyEstateCreation()
        }

        binding.addEstateTypeButton.setOnClickListener {
            onEstateTypeButtonClick()
        }

        binding.addEstatePriceSwitch.setOnClickListener {

            if (isInDollars) {
                Log.d(TAG, "wasDollars")
                initCurrency(false)

            } else {
                Log.d(TAG, "wasEuro")
                initCurrency(true)

            }
        }

        binding.addEstateAddressInput.setOnClickListener {

            /*
            val fieldList =
                Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME)

            val intent =
                Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList)
                    .build(this.applicationContext)


            val resultLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        // There are no request codes
                        val data: Intent? = result.data
                        Log.d(TAG, "data : $data")
                    }
                }

            resultLauncher.launch(intent)

             */

        }


    }


    private fun onEstateTypeButtonClick() {

        val listPopupWindow =
            ListPopupWindow(this, null, com.google.android.material.R.attr.listPopupWindowStyle)
        listPopupWindow.anchorView = binding.addEstateTypeButton

        val items: Array<String> = resources.getStringArray(R.array.estate_type_array)

        listPopupWindow.setAdapter(
            ArrayAdapter(
                this,
                android.R.layout.simple_selectable_list_item,
                items
            )
        )

        listPopupWindow.setOnItemClickListener { _, _, position: Int, _ ->


            Log.d(TAG, "was : " + estateType.name)

            estateType = when (position) {

                0 -> EstateType.HOUSE
                1 -> EstateType.APARTMENT
                2 -> EstateType.BUILDING
                3 -> EstateType.LOFT
                4 -> EstateType.CASTLE
                5 -> EstateType.BOAT
                6 -> EstateType.MANSION
                7 -> EstateType.SITE
                else -> EstateType.OTHER

            }
            Log.d(TAG, "is now : " + estateType.name)

            onTypeSelected(estateType)
            //setType

            listPopupWindow.dismiss()
        }

        listPopupWindow.show()

    }

    private fun onTypeSelected(estateType: EstateType) {
        binding.addEstateTypeButton.text = Estate.getEstateType(this, estateType)

    }


    private fun initSpinner() {

        ArrayAdapter.createFromResource(
            this,
            R.array.estate_type_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            // binding.addEstateTypeSpinner.adapter = adapter
        }

/*
        binding.addEstateTypeSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, id: Long) {
                    Log.d(TAG, "onItemSelected : $pos $id")
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    Log.d(TAG, "onNothingSelected ")
                }

            }

 */
    }

}