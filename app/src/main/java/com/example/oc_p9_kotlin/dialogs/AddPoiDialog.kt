package com.example.oc_p9_kotlin.dialogs

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.view.isEmpty
import com.example.oc_p9_kotlin.R
import com.example.oc_p9_kotlin.activities.AddEstateActivity
import com.example.oc_p9_kotlin.activities.FullScreenMapActivity
import com.example.oc_p9_kotlin.databinding.AddPoiDialogBinding
import com.example.oc_p9_kotlin.models.EstateType
import com.example.oc_p9_kotlin.models.FakePOI
import com.example.oc_p9_kotlin.models.POIType
import com.google.android.material.textfield.TextInputLayout
import java.util.UUID

object AddPoiDialog {

    private val TAG = " AddPoiDialog"
    private var poiType = POIType.STATION
    private var canCreate = true
    fun showDialog(context: Context, onConfirmClick: (FakePOI) -> Unit): AlertDialog {

        val addPoiDialogBinding =
            AddPoiDialogBinding.inflate(LayoutInflater.from(context), null, false)

        val popup = AlertDialog.Builder(context)
            .setView(addPoiDialogBinding.root)
            .create()

        popup.show()
        //popup.window?.setBackgroundDrawableResource(android.R.color.transparent)

        addPoiDialogBinding.addPoiDialogTypeButton.setOnClickListener {
            onPoiTypeButtonClick(context, addPoiDialogBinding)
        }


        addPoiDialogBinding.addPoiLocationEditText.setOnClickListener {
            Log.d(TAG, "onLocationInputClick")
            val intent =
                Intent(context, FullScreenMapActivity::class.java)
           context.startActivity(intent)
        }

        addPoiDialogBinding.addPoiConfirm.setOnClickListener {

            var requiredEditTexts = listOf(
                addPoiDialogBinding.addPoiDialogNameInput,
                addPoiDialogBinding.addPoiDialogDescriptionInput,
                addPoiDialogBinding.addPoiLocationInput,
            )
            for (editText in requiredEditTexts) {
                if (editText.editText?.text.isNullOrBlank()) {
                    editText.error = context.getString(R.string.add_estate_input_error)
                    canCreate = false
                } else {
                    editText.error = null
                }
            }

            if (!canCreate)
                return@setOnClickListener
                Log.d(TAG, "canCreate")

                var poi =  FakePOI(
                    UUID.randomUUID().toString(),
                    addPoiDialogBinding.addPoiDialogNameInput.editText?.text.toString(),
                    addPoiDialogBinding.addPoiDialogDescriptionInput.editText?.text.toString(),
                    poiType,
                    0.0,
                    0.0
                )
                Log.d(TAG, poi.toString())

                onConfirmClick(poi)



            /*

            onConfirmClick(
                FakePOI(
                    UUID.randomUUID(),
                    addPoiDialogBinding.addPoiDialogNameInput.editText?.toString(),
                    addPoiDialogBinding.addPoiDialogDescriptionInput.editText?.toString(),
                    null,
                    latitude,
                    longitude
                )
            )

             */

        }
        return popup

    }

    private fun onPoiTypeButtonClick(context: Context, binding: AddPoiDialogBinding) {

        val listPopupWindow =
            ListPopupWindow(
                context,
                null,
                com.google.android.material.R.attr.listPopupWindowStyle
            )

        listPopupWindow.anchorView = binding.addPoiDialogTypeButton

        val items: Array<String> = context.resources.getStringArray(R.array.poi_type_array)

        listPopupWindow.setAdapter(
            ArrayAdapter(
                context,
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

            binding.addPoiDialogTypeButton.text = context.getString(poiType.stringValue)
            //setType

            listPopupWindow.dismiss()
        }

        if (!listPopupWindow.isShowing)
            listPopupWindow.show()
        else
            listPopupWindow.dismiss()

    }


}