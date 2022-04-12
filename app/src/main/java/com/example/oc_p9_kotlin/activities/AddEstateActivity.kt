package com.example.oc_p9_kotlin.activities

import android.content.ActivityNotFoundException
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oc_p9_kotlin.AddEstateViewModelFactory
import com.example.oc_p9_kotlin.R
import com.example.oc_p9_kotlin.adapters.ImageAdapter
import com.example.oc_p9_kotlin.databinding.ActivityAddEstateBinding
import com.example.oc_p9_kotlin.models.Estate
import com.example.oc_p9_kotlin.models.EstateType
import com.example.oc_p9_kotlin.models.Media
import com.example.oc_p9_kotlin.utils.Utils
import com.example.oc_p9_kotlin.view_models.AddEstateViewModel
import java.util.Date
import java.util.Locale
import java.util.UUID


class AddEstateActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "AddEstateActivity"
        private const val AUTOCOMPLETE_REQUEST_CODE = 1

    }

    private lateinit var viewModel: AddEstateViewModel

    private lateinit var binding: ActivityAddEstateBinding
    private var estateType: EstateType = EstateType.HOUSE
    private var isInDollars: Boolean = true

    private lateinit var imageAdapter: ImageAdapter

    private val REQUEST_IMAGE_CAPTURE = 1
    private val PICK_IMAGE = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Setting up view with binding
        binding = ActivityAddEstateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Default type
        binding.addEstateTypeButton.text = getString(estateType.stringValue)

        initViewModels()

        // Init preferred currency (USD or EUR)
        initCurrency()

        // Setting up toolbar according to our view
        setUpToolbar()


        initListeners()
        initSpinner()
        initPics()


    }

    private fun initViewModels() {
        viewModel = ViewModelProvider(
            this, AddEstateViewModelFactory(this)
        ).get(AddEstateViewModel::class.java)

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
            binding.addEstatePriceInput.startIconDrawable =
                AppCompatResources
                    .getDrawable(this, R.drawable.ic_dollar)

            binding.addEstatePriceInput.hint = getString(R.string.add_estate_price_hint_dollars)

            this.isInDollars = true

        } else {
            binding.addEstatePriceInput.startIconDrawable =
                AppCompatResources
                    .getDrawable(this, R.drawable.ic_euro)

            binding.addEstatePriceInput.hint = getString(R.string.add_estate_price_hint_euros)
            this.isInDollars = false
        }

    }


    private fun setUpToolbar() {
        //Setting up toolbar
        binding.toolbar.overflowIcon =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_filter, null)
        binding.toolbar.navigationIcon =
            AppCompatResources.getDrawable(this, R.drawable.ic_back_arrow)

        setSupportActionBar(binding.toolbar)
    }

    private fun verifyEstateCreation() {

        var canCreate = true

        //Check if all required fields are filled
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
                editText.error = getString(R.string.add_estate_input_error)
                canCreate = false
            } else {
                editText.error = null
            }
        }

        if (canCreate) {
            Log.d(TAG, "can create Estate")
            with(binding) {
                val location = Location("")
                //TODO: Handle location managment
                location.latitude = 49.54454396
                location.longitude = 2.8484848

                // Convert in Euros if price entered in dollars
                val priceInEuros = if (isInDollars)
                    Utils().convertDollarToEuro(
                        addEstatePriceInput.editText?.text.toString().toInt()
                    )
                else
                    addEstatePriceInput.editText?.text.toString().toInt()


                // Create our new Estate with our filled fields
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
                    imageAdapter.imageList,
                    Date(),
                    null,
                    isAvailable = true,
                    null
                )

                // Add new estate to database
                viewModel.insertEstate(estate)

                Toast.makeText(
                    this@AddEstateActivity,
                    R.string.add_estate_success,
                    Toast.LENGTH_LONG
                ).show()

                finish()
                Log.d(TAG, " created : " + estate.toString())
            }
        }

    }

    private fun selectPictureIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_OPEN_DOCUMENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)
    }

    private fun takePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

    private fun initPics() {

        Log.d(TAG, "initPics")

        binding.addEstateDefaultPic.visibility = View.GONE

        imageAdapter = ImageAdapter(
            mutableListOf(),
            true
        ) {}

        binding.addEstateRecyclerView.adapter = imageAdapter
        binding.addEstateRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)


    }

/*
        estate.medias?.let {
            binding.detailsDefaultPic.visibility = View.GONE
            imageAdapter = ImageAdapter(
                it.toMutableList()
            )
            binding.detailsPicsRecyclerView.adapter = imageAdapter
            binding.detailsPicsRecyclerView.layoutManager =
                LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)


        }

 */


    private fun initListeners() {

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.addEstateAddPic.setOnClickListener {
            selectPictureIntent()
        }

        binding.addEstateTakePic.setOnClickListener {
            takePictureIntent()
        }

        binding.addEstateConfirm.setOnClickListener {
            verifyEstateCreation()
        }

        binding.addEstateTypeButton.setOnClickListener {
            onEstateTypeButtonClick()
        }

        binding.addEstatePriceInput.setStartIconOnClickListener {

            if (isInDollars) {
                Log.d(TAG, "wasDollars")
                initCurrency(false)

            } else {
                Log.d(TAG, "wasEuro")
                initCurrency(true)

            }
        }

        binding.addEstateAddressInput.setOnClickListener {


        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Log.d(TAG, "image capture : " + data?.data)
                // val imageBitmap = data?.extras?.get("data") as Bitmap
                //imageView.setImageBitmap(imageBitmap)
            }
            if (requestCode == PICK_IMAGE) {
                Log.d(TAG, "pick image : " + data?.data?.path)
                Log.d(TAG, " " + data?.data?.toString())
                Log.d(TAG, " " + data?.data?.encodedPath)
                Log.d(TAG, " " + data?.data?.schemeSpecificPart)


                var canAdd = true
                var newMedia = Media(
                    (imageAdapter.itemCount + 1).toString(),
                    data?.data.toString()
                )

                for (media in imageAdapter.imageList) {
                    if (media.url == newMedia.url) {
                        canAdd = false
                        Log.d(TAG, "ALREADY SELECTED")

                    }
                }
                //mediaList.add()
                //imageAdapter.updateData(mediaList)
                if (canAdd) {
                    createAddDialog(newMedia)
                    binding.scrollView.fullScroll(ScrollView.FOCUS_UP)


                } else {
                    Toast.makeText(this, R.string.add_estate_pic_already_added, Toast.LENGTH_LONG)
                        .show()
                }


            }
        } else {
            Log.d(TAG, "request code : $requestCode RESULT NOT OK ")

        }
    }

    private fun createAddDialog(media: Media) {

        var editText = EditText(this)
        val alert: AlertDialog.Builder = AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.add_estate_dialog_title))
            setMessage(getString(R.string.add_estate_dialog_text))
        }

        alert.setView(editText)

        alert.setPositiveButton(
            getString(R.string.add_estate_dialog_confirm)
        ) { _, _ -> //What ever you want to do with the value
            if (!editText.text.isNullOrBlank())
                media.name = editText.text.toString()
            imageAdapter.addData(media)

        }

        alert.show()
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
        binding.addEstateTypeButton.text = getString(estateType.stringValue)

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