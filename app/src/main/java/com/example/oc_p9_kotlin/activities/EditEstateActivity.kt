package com.example.oc_p9_kotlin.activities

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ScrollView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.example.oc_p9_kotlin.EditEstateViewModelFactory
import com.example.oc_p9_kotlin.R
import com.example.oc_p9_kotlin.adapters.ImageAdapter
import com.example.oc_p9_kotlin.adapters.PoiAdapter
import com.example.oc_p9_kotlin.adapters.VideoAdapter
import com.example.oc_p9_kotlin.databinding.ActivityEditEstateBinding
import com.example.oc_p9_kotlin.events.OnEstateEvent
import com.example.oc_p9_kotlin.models.Currency
import com.example.oc_p9_kotlin.models.Estate
import com.example.oc_p9_kotlin.models.EstateType
import com.example.oc_p9_kotlin.models.FakePOI
import com.example.oc_p9_kotlin.models.Media
import com.example.oc_p9_kotlin.models.POIType
import com.example.oc_p9_kotlin.utils.FileUtils
import com.example.oc_p9_kotlin.utils.Utils
import com.example.oc_p9_kotlin.view_models.EditEstateViewModel
import io.reactivex.rxjava3.kotlin.addTo
import org.greenrobot.eventbus.EventBus
import java.io.File
import java.io.IOException
import java.io.Serializable
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale
import java.util.UUID


class EditEstateActivity : CompositeDisposableActivity() {

    companion object {

        private const val TAG = "EditEstateActivity"

        lateinit var poiAdapter: PoiAdapter
        private var onEstateEvent = OnEstateEvent()
        private var poiList = mutableListOf<FakePOI>()

    }

    private lateinit var viewModel: EditEstateViewModel
    private lateinit var binding: ActivityEditEstateBinding

    private lateinit var videoAdapter: VideoAdapter
    private lateinit var imageAdapter: ImageAdapter

    private lateinit var estate: Estate
    private var estateType: EstateType = EstateType.HOUSE
    private var currency: Currency? = null
    private lateinit var location: Location
    private var saleDate: Date? = Date()
    private var isEditing: Boolean = false

    private val REQUEST_IMAGE_CAPTURE = 1
    private val PICK_IMAGE = 2
    private val PICK_VIDEO = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Setting up view with binding
        binding = ActivityEditEstateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Default type
        binding.addEstateTypeButton.text = getString(estateType.stringValue)

        initViewModels()

        // Init preferred currency (USD or EUR)
        initCurrency()

        // Setting up toolbar according to our view
        setUpToolbar()

        initListeners()
        initPicsRecyclerView()
        initVideosRecyclerView()
        initPoiRecyclerView()

        //Get estate for edition view
        if (intent?.getSerializableExtra("estate") != null) {
            estate = intent?.getSerializableExtra("estate") as Estate
            initFields(estate)
        }
    }

    private fun initFields(estate: Estate?) {

        if (estate == null)
            return

        val saleDateCalendar: Calendar = GregorianCalendar()

        if (estate.saleDate != null) {
            Log.d(TAG, "sale date : " + estate.saleDate)
            saleDate = estate.saleDate
            saleDateCalendar.time = saleDate
        }

        isEditing = true
        estateType = estate.type
        binding.addEstateTypeButton.text = getString(estate.type.stringValue)
        location = estate.location
        binding.addEstateCityInput.editText?.setText(estate.city)
        binding.addEstateAddressInput.editText?.setText(estate.address)

        if (currency == Currency.DOLLAR)
            binding.addEstatePriceInput.editText?.setText(
                Utils.convertEuroToDollars(estate.priceInEuros).toString()
            )
        else
            binding.addEstatePriceInput.editText?.setText(estate.priceInEuros.toString())

        binding.addEstateSurfaceInput.editText?.setText(estate.surfaceInSquareMeters.toString())
        binding.addEstateRoomsInput.editText?.setText(estate.rooms.toString())
        binding.addEstateBathroomsInput.editText?.setText(estate.bathrooms.toString())
        binding.addEstateBedroomsInput.editText?.setText(estate.bedrooms.toString())
        binding.addEstateDescriptionInput.editText?.setText(estate.description.toString())
        binding.addEstateLocationInput.editText?.setText(
            getString(
                R.string.add_estate_location,
                estate.location.latitude.toString(),
                estate.location.longitude.toString()
            )
        )

        estate.medias?.let {
            imageAdapter.updateData(it.toMutableList())
        }
        estate.videos?.let {
            videoAdapter.updateData(it.toMutableList())
        }
        estate.nearbyPlaces?.let {
            poiAdapter.updateData(it.toMutableList())
        }

        binding.addEstateAvailableCheckbox.isChecked = estate.isAvailable
    }

    private fun initViewModels() {
        viewModel = ViewModelProvider(
            this, EditEstateViewModelFactory(this)
        ).get(EditEstateViewModel::class.java)
    }

    private fun initCurrency(currency: Currency? = null) {
        //Handle currency according to user selection or default language
        when (currency) {
            Currency.DOLLAR -> setCurrencyInDollars()
            Currency.EURO -> setCurrencyInEuros()
            null -> setCurrencyFromLocale()
        }
    }

    private fun setCurrencyFromLocale() {
        if (Locale.getDefault().language == Locale.FRENCH.language) {
            setCurrencyInEuros()
        } else {
            setCurrencyInDollars()
        }
    }

    private fun setCurrencyInDollars() {
        binding.addEstatePriceInput.startIconDrawable =
            AppCompatResources
                .getDrawable(this, R.drawable.ic_dollar)
        binding.addEstatePriceInput.hint = getString(R.string.add_estate_price_hint_dollars)
        this.currency = Currency.DOLLAR
    }

    private fun setCurrencyInEuros() {
        binding.addEstatePriceInput.startIconDrawable =
            AppCompatResources
                .getDrawable(this, R.drawable.ic_euro)
        binding.addEstatePriceInput.hint = getString(R.string.add_estate_price_hint_euros)
        this.currency = Currency.EURO
    }

    private fun setUpToolbar() {
        binding.toolbar.overflowIcon =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_filter, null)
        binding.toolbar.navigationIcon =
            AppCompatResources.getDrawable(this, R.drawable.ic_back_arrow)

        setSupportActionBar(binding.toolbar)
    }


    private fun verifyEstateEdition() {

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
            binding.addEstateLocationInput,
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

        if (!canCreate)
            return

        with(binding) {

            // Convert in euros if the entered price is in dollars
            val priceInEuros = if (currency == Currency.DOLLAR) {
                Utils.convertDollarToEuro(addEstatePriceInput.editText?.text.toString().toInt())
            } else {
                addEstatePriceInput.editText?.text.toString().toInt()
            }

            if (isEditing) {
                //Update the selected estate if we are in edition mode
                estate.type = estateType
                estate.city = addEstateCityInput.editText?.text.toString()
                estate.address = addEstateAddressInput.editText?.text.toString()
                estate.priceInEuros = priceInEuros
                estate.surfaceInSquareMeters =
                    addEstateSurfaceInput.editText?.text.toString().toInt()
                estate.rooms = addEstateRoomsInput.editText?.text.toString().toInt()
                estate.bathrooms = addEstateBathroomsInput.editText?.text.toString().toInt()
                estate.bedrooms = addEstateBedroomsInput.editText?.text.toString().toInt()
                estate.description = addEstateDescriptionInput.editText?.text.toString()
                estate.location = location
                estate.nearbyPlaces = poiList
                estate.medias = imageAdapter.imageList
                estate.videos = videoAdapter.videoList

                if (binding.addEstateAvailableCheckbox.isChecked) {
                    estate.isAvailable = true
                    estate.saleDate = null
                } else {
                    estate.isAvailable = false
                    estate.saleDate = saleDate
                }

                viewModel.updateEstate(estate)
                    .subscribe()
                    .addTo(bag)

                onEstateEvent.setSelectedEstate(estate)
                EventBus.getDefault().postSticky(onEstateEvent)

                finish()

            } else {

                // Create a new Estate with our filled fields
                val estate = Estate(
                    UUID.randomUUID().toString(),
                    estateType,
                    addEstateCityInput.editText?.text.toString(),
                    priceInEuros,
                    addEstateSurfaceInput.editText?.text.toString().toInt(),
                    addEstateRoomsInput.editText?.text.toString().toInt(),
                    addEstateBathroomsInput.editText?.text.toString().toInt(),
                    addEstateBedroomsInput.editText?.text.toString().toInt(),
                    addEstateAddressInput.editText?.text.toString(),
                    location,
                    poiList,
                    isTypeIncluded(poiList, POIType.STATION),
                    isTypeIncluded(poiList, POIType.PUB),
                    isTypeIncluded(poiList, POIType.HOSTEL),
                    isTypeIncluded(poiList, POIType.HOSPITAL),
                    isTypeIncluded(poiList, POIType.SCHOOL),
                    isTypeIncluded(poiList, POIType.PARK),
                    isTypeIncluded(poiList, POIType.RESTAURANT),
                    isTypeIncluded(poiList, POIType.OTHER),
                    addEstateDescriptionInput.editText?.text.toString(),
                    imageAdapter.imageList,
                    imageAdapter.imageList.size,
                    videoAdapter.videoList,
                    Date(),
                    null,
                    isAvailable = true,
                    null
                )
                viewModel.insertEstate(estate)
                    .subscribe()
                    .addTo(bag)

                finish()

            }
        }
    }

    private fun isTypeIncluded(poiList: List<FakePOI>, poiType: POIType): Boolean {
        //Verify if a specific type is included to estate's poi list
        var isIncluded = false
        for (fakePoi in poiList) {
            if (fakePoi.poiType == poiType)
                isIncluded = true
        }
        return isIncluded

    }


    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed

    var selectPictureLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { result ->
        result?.let {
            val newMedia = Media(
                (imageAdapter.itemCount + 1).toString(),
                result.toString()
            )
            verifyAndAddMedia(newMedia)
        }
    }

    var selectVideoLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { result ->
        result?.let {
            val uri: Uri = it
            val file: File = FileUtils.from(this, uri)
            val finalUri = file.toURI()
            val newMedia = Media(
                (imageAdapter.itemCount + 1).toString(),
                finalUri.toString()
            )
            verifyAndAddMedia(newMedia, true)
        }

    }

    var takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        result?.let {
            val imageBitmap = result.data?.extras?.get("data") as? Bitmap ?: return@let
            val uri = Utils.getImageUri(this, imageBitmap).toString()
            val newMedia = Media(
                (imageAdapter.itemCount + 1).toString(),
                uri
            )
            verifyAndAddMedia(newMedia)
        }

    }

    private fun initPoiRecyclerView() {

        poiAdapter = PoiAdapter(
            mutableListOf(),
            true,
        ) {
            // ON DATA UPDATE
            poiList = poiAdapter.poiList
        }
        binding.addEstatePoiRecyclerView.adapter = poiAdapter
    }

    private fun initPicsRecyclerView() {

        imageAdapter = ImageAdapter(
            mutableListOf(),
            true,
            {
                //ON DATA UPDATE
                verifyPlaceHolders()
            },
            {
                viewFullscreen(imageAdapter.imageList)
            }
        )
        binding.addEstateRecyclerView.adapter = imageAdapter

    }

    private fun initVideosRecyclerView() {

        videoAdapter = VideoAdapter(
            mutableListOf(),
            true
        ) {
            //ON DATA UPDATE
            verifyPlaceHolders()

        }
        binding.addEstateVideoRecyclerView.adapter = videoAdapter


    }


    private fun viewFullscreen(medias: List<Media>) {
        Log.d(TAG, "start Full Screen Activity")
        val list: List<Media> = medias
        val intent =
            Intent(binding.root.context, FullScreenPictureActivity::class.java)
        intent.putExtra("medias", list as Serializable)

        startActivity(intent)
    }

    private fun verifyPlaceHolders() {

        if (imageAdapter.itemCount > 0) {
            binding.addEstateDefaultPic.visibility = View.GONE
            binding.addEstatePhotoTitle.visibility = View.VISIBLE

        } else {
            binding.addEstateDefaultPic.visibility = View.VISIBLE
            binding.addEstatePhotoTitle.visibility = View.GONE

        }
        if (videoAdapter.itemCount > 0) {
            binding.addEstateVideoTitle.visibility = View.VISIBLE
            binding.addEstateVideoRecyclerView.visibility = View.VISIBLE
        } else {
            binding.addEstateVideoTitle.visibility = View.GONE
            binding.addEstateVideoRecyclerView.visibility = View.GONE
        }

    }


    private fun initListeners() {

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.addEstateAddPic.setOnClickListener {
            //Select a picture from the device gallery
            selectPictureLauncher.launch("image/*")
        }

        binding.addEstateTakePic.setOnClickListener {
            //Take a picture from camera
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if(intent.resolveActivity(packageManager) != null ){
                takePictureLauncher.launch(intent)
                //takePictureIntent()
            }

        }

        binding.addEstateAddVideo.setOnClickListener {
            //Select a video from the device gallery
            //selectVideoIntent()
            selectVideoLauncher.launch("video/*")

        }

        binding.addEstateConfirm.setOnClickListener {
            verifyEstateEdition()
        }

        binding.addEstateTypeButton.setOnClickListener {
            onEstateTypeButtonClick()
        }

        binding.addEstatePriceInput.setStartIconOnClickListener {
            //Manually switch the currency (dollar or USD)
            if (currency == Currency.DOLLAR) {
                initCurrency(Currency.EURO)
            } else {
                initCurrency(Currency.DOLLAR)
            }
        }

        binding.addEstateLocationEditText.setOnClickListener {
            val intent =
                Intent(this, FullScreenMapActivity::class.java)
            startActivity(intent)
        }

        binding.addEstateAddPoi.setOnClickListener {
            val intent =
                Intent(this, AddPoiActivity::class.java)
            startActivity(intent)
        }

        binding.addEstateAvailableCheckbox.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                binding.addEstateDateSaleTitle.visibility = View.GONE
                binding.addEstateDateSalePicker.visibility = View.GONE
            } else {
                binding.addEstateDateSaleTitle.visibility = View.VISIBLE
                binding.addEstateDateSalePicker.visibility = View.VISIBLE

            }

            val saleDateCalendar: Calendar = GregorianCalendar()

            binding.addEstateDateSalePicker.init(
                saleDateCalendar.get(Calendar.YEAR),
                saleDateCalendar.get(Calendar.MONTH),
                saleDateCalendar.get(Calendar.DAY_OF_MONTH)
            ) { datePicker, year, month, day ->
                saleDateCalendar.set(
                    datePicker.year,
                    datePicker.month,
                    datePicker.dayOfMonth
                )
                saleDate = saleDateCalendar.time
            }
        }
    }

    override fun onResume() {
        super.onResume()

        verifyPlaceHolders()

        if (FullScreenMapActivity.marker != null) {
            val latitude = FullScreenMapActivity.marker?.position?.latitude
            val longitude = FullScreenMapActivity.marker?.position?.longitude
            location = Location("0")
            if (latitude != null && longitude != null) {
                location.latitude = latitude
                location.longitude = longitude
            }
            binding.addEstateLocationEditText.setText(
                getString(
                    R.string.add_estate_location,
                    latitude?.toString(),
                    longitude?.toString()
                )
            )
            FullScreenMapActivity.marker = null
        }

    }

    private fun verifyAndAddMedia(newMedia: Media, isVideo: Boolean = false) {
        //Add media if he's not already in the list
        var canAdd = true

        if (isVideo) {
            for (media in videoAdapter.videoList) {
                if (media.uri == newMedia.uri) {
                    canAdd = false
                }
            }
        } else {
            for (media in imageAdapter.imageList) {
                if (media.uri == newMedia.uri) {
                    canAdd = false
                }
            }
        }
        if (canAdd) {
            createAddDialog(newMedia, isVideo)
            binding.scrollView.fullScroll(ScrollView.FOCUS_UP)
        } else {
            Toast.makeText(
                this,
                R.string.add_estate_pic_already_added,
                Toast.LENGTH_LONG
            )
                .show()
        }
    }

    private fun createAddDialog(newMedia: Media, isVideo: Boolean) {
        //Show dialog to confirm the media to add
        val editText = EditText(this)

        val alert: AlertDialog.Builder = AlertDialog.Builder(this).apply {
            if (isVideo)
                setTitle(getString(R.string.add_estate_dialog_video))
            else
                setTitle(getString(R.string.add_estate_dialog_title))
            setMessage(getString(R.string.add_estate_dialog_text))

        }

        alert.setView(editText)
        alert.setCancelable(false)

        alert.setPositiveButton(
            getString(R.string.add_estate_dialog_confirm)
        ) { _, _ -> //What ever you want to do with the value
            if (!editText.text.isNullOrBlank())
                newMedia.name = editText.text.toString()

            if (isVideo) {
                videoAdapter.addData(newMedia)

            } else {
                if (binding.addEstateDefaultPic.visibility == View.VISIBLE)
                    binding.addEstateDefaultPic.visibility = View.GONE

                imageAdapter.addData(newMedia)

            }

        }
        alert.show()
    }


    private fun onEstateTypeButtonClick() {
        //Change the estate type on button click
        val listPopupWindow =
            ListPopupWindow(
                this,
                null,
                com.google.android.material.R.attr.listPopupWindowStyle
            )
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
            binding.addEstateTypeButton.text = getString(estateType.stringValue)
            listPopupWindow.dismiss()
        }

        if (!listPopupWindow.isShowing)
            listPopupWindow.show()
        else
            listPopupWindow.dismiss()

    }

}