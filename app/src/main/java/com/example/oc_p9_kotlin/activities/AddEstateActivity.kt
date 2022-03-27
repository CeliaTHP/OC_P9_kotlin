package com.example.oc_p9_kotlin.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import com.example.oc_p9_kotlin.R
import com.example.oc_p9_kotlin.databinding.ActivityAddEstateBinding
import com.example.oc_p9_kotlin.databinding.ActivityMainBinding

class AddEstateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEstateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddEstateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.overflowIcon =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_filter, null)
        binding.toolbar.navigationIcon =
            AppCompatResources.getDrawable(this, R.drawable.ic_back_arrow)
        setSupportActionBar(binding.toolbar)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }


    }
}