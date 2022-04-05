package com.example.oc_p9_kotlin.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.content.res.ResourcesCompat
import com.example.oc_p9_kotlin.R
import com.example.oc_p9_kotlin.databinding.ActivityAddEstateBinding
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import java.util.Arrays


class AddEstateActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "AddEstateActivity"
        private const val AUTOCOMPLETE_REQUEST_CODE = 1

    }

    private lateinit var binding: ActivityAddEstateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Setting up view with binding
        binding = ActivityAddEstateBinding.inflate(layoutInflater)
        setContentView(binding.root)



        initPlaces()
        initToolbar()
        initListeners()
        initSpinner()

    }

    private fun initPlaces() {

    }



    private fun initToolbar() {
        //Setting up toolbar
        binding.toolbar.overflowIcon =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_filter, null)
        binding.toolbar.navigationIcon =
            AppCompatResources.getDrawable(this, R.drawable.ic_back_arrow)

        setSupportActionBar(binding.toolbar)
    }

    private fun initListeners() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.fab.setOnClickListener {
            Log.d(TAG, "can Create Estate")
        }

        binding.addEstateTypeButton.setOnClickListener {
            onEstateTypeButtonClick()
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
        listPopupWindow.setAdapter(
            ArrayAdapter(
                this,
                R.layout.dropdown_menu_popup_item,
                arrayOf("type1", "type2", "type3")
            )
        )

        listPopupWindow.setOnItemClickListener { _, _, position: Int, _ ->
            //callback(position)
            //setType
            listPopupWindow.dismiss()
        }
        listPopupWindow.show()


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