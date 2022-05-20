package com.example.oc_p9_kotlin.activities

import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.widget.ListPopupWindow
import com.example.oc_p9_kotlin.databinding.AddPoiDialogBinding
import com.example.oc_p9_kotlin.models.FakePOI
import com.example.oc_p9_kotlin.models.POIType
import com.google.android.material.R
import java.util.UUID

class AddPoiActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "AddPoiActivity"

    }

    private lateinit var binding: AddPoiDialogBinding

    private var poiType = POIType.STATION

    private var latitude = 0.0
    private var longitude = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = AddPoiDialogBinding.inflate(layoutInflater)

        //Default type
        binding.addPoiDialogTypeButton.text = getString(poiType.stringValue)

        initListeners() 
        setContentView(binding.root)

    }

    private fun initListeners() {

        binding.addPoiDialogTypeButton.setOnClickListener {
            onPoiTypeButtonClick()
        }


        binding.addPoiLocationEditText.setOnClickListener {
            Log.d(TAG, "onLocationInputClick")
            val intent =
                Intent(this, FullScreenMapActivity::class.java)
            startActivity(intent)
        }
        binding.addPoiConfirm.setOnClickListener {
            verifyPoiCreation()


        }
    }
    private fun verifyPoiCreation() {

        var canCreate = true

        var requiredEditTexts = listOf(
            binding.addPoiDialogNameInput,
            binding.addPoiDialogDescriptionInput,
            binding.addPoiLocationInput,
        )
        for (editText in requiredEditTexts) {
            if (editText.editText?.text.isNullOrBlank()) {
                editText.error =
                    getString(com.example.oc_p9_kotlin.R.string.add_estate_input_error)
                canCreate = false
            } else {
                editText.error = null
            }
        }

        if (!canCreate)
            return

        Log.d(TAG, "canCreate")

        var poi = FakePOI(
            UUID.randomUUID().toString(),
            binding.addPoiDialogNameInput.editText?.text.toString(),
            binding.addPoiDialogDescriptionInput.editText?.text.toString(),
            poiType,
            latitude,
            longitude
        )




    }

    override fun onResume() {
        super.onResume()

        if (FullScreenMapActivity.marker != null) {
            var latitude = FullScreenMapActivity.marker?.position?.latitude
            var longitude = FullScreenMapActivity.marker?.position?.longitude

          var   location = Location("0")
            if (latitude != null && longitude != null) {
                location.latitude = latitude
                location.longitude = longitude
            }
            Log.d(TAG, location.toString())
            binding.addPoiLocationEditText.setText(
                getString(
                    com.example.oc_p9_kotlin.R.string.add_estate_location,
                    latitude?.toString(),
                    longitude?.toString()
                )
            )

            FullScreenMapActivity.marker= null
        }
    }


        private fun onPoiTypeButtonClick() {

            val listPopupWindow =
                ListPopupWindow(
                    this,
                    null,
                    R.attr.listPopupWindowStyle
                )

            listPopupWindow.anchorView = binding.addPoiDialogTypeButton

            val items: Array<String> =
                resources.getStringArray(com.example.oc_p9_kotlin.R.array.poi_type_array)

            listPopupWindow.setAdapter(
                ArrayAdapter(
                    this,
                    android.R.layout.simple_selectable_list_item,
                    items
                )
            )

            listPopupWindow.setOnItemClickListener { _, _, position: Int, _ ->


                Log.d(TAG, "was : " + poiType.name)

                poiType = when (position) {

                    0 -> POIType.STATION
                    1 -> POIType.PUB
                    2 -> POIType.HOSTEL
                    3 -> POIType.HOSPITAL
                    4 -> POIType.SCHOOL
                    5 -> POIType.PARK
                    6 -> POIType.RESTAURANT
                    7 -> POIType.OTHER
                    else -> POIType.OTHER

                }

                Log.d(TAG, "is now : " + poiType.name)

                binding.addPoiDialogTypeButton.text = getString(poiType.stringValue)
                //setType

                listPopupWindow.dismiss()
            }

            if (!listPopupWindow.isShowing)
                listPopupWindow.show()
            else
                listPopupWindow.dismiss()

        }
    }
    
    
    
    