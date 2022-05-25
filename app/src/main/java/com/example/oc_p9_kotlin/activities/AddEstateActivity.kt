package com.example.oc_p9_kotlin.activities

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.location.Location
import android.media.metrics.Event
import android.net.Uri
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
import com.example.oc_p9_kotlin.AddEstateViewModelFactory
import com.example.oc_p9_kotlin.R
import com.example.oc_p9_kotlin.adapters.ImageAdapter
import com.example.oc_p9_kotlin.adapters.PoiAdapter
import com.example.oc_p9_kotlin.adapters.VideoAdapter
import com.example.oc_p9_kotlin.databinding.ActivityAddEstateBinding
import com.example.oc_p9_kotlin.events.OnEstateEvent
import com.example.oc_p9_kotlin.models.Currency
import com.example.oc_p9_kotlin.models.Estate
import com.example.oc_p9_kotlin.models.EstateType
import com.example.oc_p9_kotlin.models.FakePOI
import com.example.oc_p9_kotlin.models.Media
import com.example.oc_p9_kotlin.utils.FileUtil
import com.example.oc_p9_kotlin.utils.Utils
import com.example.oc_p9_kotlin.view_models.AddEstateViewModel
import org.greenrobot.eventbus.EventBus
import java.io.File
import java.io.IOException
import java.io.Serializable
import java.util.Date
import java.util.Locale
import java.util.UUID


class AddEstateActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "AddEstateActivity"

        /*
                fun addPoi(fakePOI: FakePOI) {
                    if (!poiList.contains(fakePOI)) {

                        Log.d(TAG, "added " + fakePOI)
                        Log.d(TAG, "list is now   " + poiList.size+ "-" + poiList)

                    } else {
                        Log.d(TAG, "$fakePOI already in the list")

                    }
                }


         */
        lateinit var poiAdapter: PoiAdapter
        private var onEstateEvent = OnEstateEvent()

        private var poiList = mutableListOf<FakePOI>()

    }

    private lateinit var viewModel: AddEstateViewModel

    private lateinit var binding: ActivityAddEstateBinding
    private var estateType: EstateType = EstateType.HOUSE
    private var currency: Currency? = null

    private lateinit var location: Location

    private var videoList = emptyList<Media>()

    private lateinit var videoAdapter: VideoAdapter

    private lateinit var imageAdapter: ImageAdapter

    private lateinit var estate: Estate

    private var isEditing: Boolean = false
    private val REQUEST_IMAGE_CAPTURE = 1
    private val PICK_IMAGE = 2
    private val PICK_VIDEO = 3


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
        initPicsRecyclerView()
        initVideosRecyclerView()
        initPoiRecyclerView()


        if (intent?.getSerializableExtra("estate") != null) {
            Log.d(TAG, "estate Found ")
            estate = intent?.getSerializableExtra("estate") as Estate
            initFields(estate)

        }


    }



    private fun initFields(estate: Estate?) {
        if (estate == null)
            return

        isEditing = true

        estateType = estate.type
        binding.addEstateTypeButton.text = getString(estate.type.stringValue)

        location = estate.location

        binding.addEstateCityInput.editText?.setText(estate.city)
        binding.addEstateAddressInput.editText?.setText(estate.address)
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

    }

    private fun initViewModels() {
        viewModel = ViewModelProvider(
            this, AddEstateViewModelFactory(this)
        ).get(AddEstateViewModel::class.java)

    }


    private fun initCurrency(currency: Currency? = null) {
        when (currency) {
            Currency.DOLLAR -> setCurrencyInDollars()
            Currency.EURO -> setCurrencyInEuros()
            null -> setCurrencyFromLocale()
        }
    }

    private fun setCurrencyFromLocale() {
        Log.d(TAG, "no option, default currency used")
        if (Locale.getDefault().language == Locale.FRENCH.language) {
            setCurrencyInEuros()
        } else {
            setCurrencyInDollars()
        }

    }

    private fun setCurrencyInDollars() {
        Log.d(TAG, "should be Dollars")
        binding.addEstatePriceInput.startIconDrawable =
            AppCompatResources
                .getDrawable(this, R.drawable.ic_dollar)
        binding.addEstatePriceInput.hint = getString(R.string.add_estate_price_hint_dollars)
        this.currency = Currency.DOLLAR

    }

    private fun setCurrencyInEuros() {
        Log.d(TAG, "should be Euros")
        binding.addEstatePriceInput.startIconDrawable =
            AppCompatResources
                .getDrawable(this, R.drawable.ic_euro)
        binding.addEstatePriceInput.hint = getString(R.string.add_estate_price_hint_euros)
        this.currency = Currency.EURO

    }


    private fun setUpToolbar() {
        //Setting up toolbar
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

        Log.d(TAG, "can create Estate")

        with(binding) {

            // Convert in Euros if price entered in dollars
            val priceInEuros = if (currency == Currency.DOLLAR) {
                Utils.convertDollarToEuro(addEstatePriceInput.editText?.text.toString().toInt())
            } else {
                addEstatePriceInput.editText?.text.toString().toInt()
            }

            if (isEditing) {
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


                viewModel.updateEstate(estate)
                Log.d(TAG, " updated : " + estate.toString())

                onEstateEvent.setSelectedEstate(estate)
                EventBus.getDefault().postSticky(onEstateEvent)

                finish()



            } else {
                // Create our new Estate with our filled fields
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
                Log.d(TAG, " created : " + estate.toString())
                finish()

            }


        }

    }

    private fun selectPictureIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Select Picture"),
            PICK_IMAGE
        )
    }

    private fun selectVideoIntent() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "video/*"

        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO)
    }

    private fun takePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

    private fun initPoiRecyclerView() {
        Log.d(TAG, "initPois")

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

        Log.d(TAG, "initPics")

        imageAdapter = ImageAdapter(
            mutableListOf(),
            true,
            { verifyPlaceholders() },
            {
                viewFullscreen(imageAdapter.imageList)
            }
        )
        binding.addEstateRecyclerView.adapter = imageAdapter


    }

    private fun initVideosRecyclerView() {

        Log.d(TAG, "initVideos")

        videoAdapter = VideoAdapter(
            mutableListOf(),
            true
        ) {
            //ON DATA UPDATE
            Log.d(TAG, "Videos onDataUpdate")//ON DATA UPDATE
            //verifyPlaceholders()
            if (videoAdapter.videoList.isNullOrEmpty()) {
                binding.addEstateVideoTitle.visibility = View.GONE
                binding.addEstateVideoRecyclerView.visibility = View.GONE

            } else {
                binding.addEstateVideoTitle.visibility = View.VISIBLE
                binding.addEstateVideoRecyclerView.visibility = View.VISIBLE

            }
        }
        binding.addEstateVideoRecyclerView.adapter = videoAdapter


    }


    private fun viewFullscreen(medias: List<Media>) {
        Log.d(TAG, "start Full Screen Activity")
        val list: List<Media> = medias
        val intent =
            Intent(binding.root.context, FullScreenPictureActivity::class.java)
        intent.putExtra("medias", list as Serializable)
        // intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        startActivity(intent)
    }

    private fun verifyPlaceholders() {
        if (imageAdapter.itemCount > 0) {
            Log.d(TAG, "one image atleast")
            binding.addEstateDefaultPic.visibility = View.GONE
            binding.addEstatePhotoTitle.visibility = View.VISIBLE

        } else {
            Log.d(TAG, "less than 1 image")
            binding.addEstateDefaultPic.visibility = View.VISIBLE
            binding.addEstatePhotoTitle.visibility = View.GONE

        }
    }


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

        binding.addEstateAddVideo.setOnClickListener {
            selectVideoIntent()
        }

        binding.addEstateConfirm.setOnClickListener {
            verifyEstateEdition()
        }

        binding.addEstateTypeButton.setOnClickListener {
            onEstateTypeButtonClick()
        }

        binding.addEstatePriceInput.setStartIconOnClickListener {

            if (currency == Currency.DOLLAR) {
                Log.d(TAG, "wasDollars")
                initCurrency(Currency.EURO)

            } else {
                Log.d(TAG, "wasEuro")
                initCurrency(Currency.DOLLAR)

            }
        }

        binding.addEstateLocationEditText.setOnClickListener {
            Log.d(TAG, "onLocationInputClick")
            val intent =
                Intent(this, FullScreenMapActivity::class.java)
            startActivity(intent)
        }

        binding.addEstateAddPoi.setOnClickListener {
            val intent =
                Intent(this, AddPoiActivity::class.java)
            startActivity(intent)
        }



    }

    override fun onResume() {
        super.onResume()

        verifyPlaceholders()

        if (FullScreenMapActivity.marker != null) {
            Log.d(TAG, FullScreenMapActivity.marker.toString())
            var latitude = FullScreenMapActivity.marker?.position?.latitude
            var longitude = FullScreenMapActivity.marker?.position?.longitude

            location = Location("0")
            if (latitude != null && longitude != null) {
                location.latitude = latitude
                location.longitude = longitude
            }
            Log.d(TAG, location.toString())
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {

                val imageBitmap = data?.extras?.get("data") as Bitmap
                Log.d(TAG, "imageBitmap : " + imageBitmap.toString())


                var uri = Utils.getImageUri(this, imageBitmap).toString()

                Log.d(TAG, "imageURI : " + uri)


                var newMedia = Media(
                    (imageAdapter.itemCount + 1).toString(),
                    uri
                )
                verifyAndAddMedia(newMedia)

            }

            if (requestCode == PICK_IMAGE) {

                Log.d(TAG, "pick image : " + data?.data?.path)
                Log.d(TAG, " " + data?.data?.toString())
                Log.d(TAG, " " + data?.data?.encodedPath)
                Log.d(TAG, " " + data?.data?.schemeSpecificPart)

                if (data == null) {
                    //error
                    return
                }
                try {
                    data.data?.let {
                        val uri: Uri = it
                        val file: File = FileUtil.from(this, uri)

                        var finalUri = file.toURI()
                        Log.d(TAG, "final URI : " + finalUri)


                        var newMedia = Media(
                            (imageAdapter.itemCount + 1).toString(),
                            finalUri.toString()
                        )

                        verifyAndAddMedia(newMedia)
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                }


            }
            if (requestCode == PICK_VIDEO) {

                if (data == null) {
                    //error
                    return
                }

                try {
                    data.data?.let {
                        val uri: Uri = it
                        val file: File = FileUtil.from(this, uri)


                        var finalUri = file.toURI()
                        Log.d(TAG, "final URI : " + finalUri)


                        var newMedia = Media(
                            (videoAdapter.itemCount + 1).toString(),
                            finalUri.toString()
                        )

                        verifyAndAddMedia(newMedia, true)
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                }

/*
                // OI FILE Manager
                var filemanagerstring = selectedImageUri?.path

                // MEDIA GALLERY
                var selectedImagePath = Utils().getRealPathFromURI(selectedImageUri, this)
                if (selectedImagePath != null) {
                    Log.d(TAG, selectedImagePath.toString())



                }

 */
            }
        } else {
            Log.d(TAG, "request code : $requestCode RESULT NOT OK ")

        }
    }


    private fun verifyAndAddMedia(newMedia: Media, isVideo: Boolean = false) {
        var canAdd = true

        Log.d(TAG, "newMedia : " + newMedia.toString())


        if (isVideo) {
            Log.d(TAG, "isVideo")
            for (media in videoAdapter.videoList) {
                if (media.uri == newMedia.uri) {
                    canAdd = false
                    Log.d(TAG, "ALREADY SELECTED")

                }
            }
        } else {
            Log.d(TAG, "isPhoto")

            for (media in imageAdapter.imageList) {
                if (media.uri == newMedia.uri) {
                    canAdd = false
                    Log.d(TAG, "ALREADY SELECTED")

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

        var editText = EditText(this)

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
                Log.d(TAG, "added Video")
                videoAdapter.addData(newMedia)

            } else {
                Log.d(TAG, "added Photo")

                if (binding.addEstateDefaultPic.visibility == View.VISIBLE)
                    binding.addEstateDefaultPic.visibility = View.GONE

                imageAdapter.addData(newMedia)

            }

            Log.d(TAG, "added media :" + newMedia.toString())

        }
        alert.show()
    }


    private fun onEstateTypeButtonClick() {

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

            binding.addEstateTypeButton.text = getString(estateType.stringValue)
            //setType

            listPopupWindow.dismiss()
        }

        if (!listPopupWindow.isShowing)
            listPopupWindow.show()
        else
            listPopupWindow.dismiss()

    }

    override fun onPause() {
        super.onPause()
    }


}