package com.example.oc_p9_kotlin.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.content.res.ResourcesCompat
import com.example.oc_p9_kotlin.R
import com.example.oc_p9_kotlin.databinding.ActivityAddEstateBinding


class AddEstateActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "AddEstateActivity"
    }

    private lateinit var binding: ActivityAddEstateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Setting up view with binding
        binding = ActivityAddEstateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Setting up toolbar
        binding.toolbar.overflowIcon =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_filter, null)
        binding.toolbar.navigationIcon =
            AppCompatResources.getDrawable(this, R.drawable.ic_back_arrow)

        setSupportActionBar(binding.toolbar)

        initListeners()

        initSpinner()


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