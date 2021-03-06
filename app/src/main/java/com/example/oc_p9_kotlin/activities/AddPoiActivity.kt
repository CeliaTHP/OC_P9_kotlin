package com.example.oc_p9_kotlin.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.widget.ListPopupWindow
import com.example.oc_p9_kotlin.databinding.ActivityAddPoiBinding
import com.example.oc_p9_kotlin.models.FakePOI
import com.example.oc_p9_kotlin.models.POIType
import com.google.android.material.R
import java.util.UUID

class AddPoiActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "AddPoiActivity"
    }

    var poi: FakePOI =
        FakePOI(
            UUID.randomUUID().toString(),
            "",
            "",
            POIType.STATION,
            0.0,
            0.0
        )

    private lateinit var binding: ActivityAddPoiBinding
    private var poiType = POIType.STATION

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddPoiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Default type
        binding.addPoiDialogTypeButton.text = getString(poiType.stringValue)

        initListeners()

    }

    private fun initListeners() {

        binding.addPoiDialogTypeButton.setOnClickListener {
            //Change poi Type on button click
            onPoiTypeButtonClick()
        }


        binding.addPoiLocationEditText.setOnClickListener {
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

        val requiredEditTexts = listOf(
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

        poi.name = binding.addPoiDialogNameInput.editText?.text.toString()
        poi.description = binding.addPoiDialogDescriptionInput.editText?.text.toString()
        poi.poiType = poiType

        EditEstateActivity.poiAdapter.addData(poi)
        finish()

    }

    override fun onResume() {
        super.onResume()

        if (FullScreenMapActivity.marker != null) {

            FullScreenMapActivity.marker?.position?.latitude?.let {
                poi.latitude = it
            }
            FullScreenMapActivity.marker?.position?.longitude?.let {
                poi.longitude = it
            }
            binding.addPoiLocationEditText.setText(
                getString(
                    com.example.oc_p9_kotlin.R.string.add_estate_location,
                    poi.latitude.toString(),
                    poi.longitude.toString()
                )
            )
            FullScreenMapActivity.marker = null
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
            binding.addPoiDialogTypeButton.text = getString(poiType.stringValue)
            listPopupWindow.dismiss()
        }

        if (!listPopupWindow.isShowing)
            listPopupWindow.show()
        else
            listPopupWindow.dismiss()

    }
}
    
    
    
    
