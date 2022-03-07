package com.example.oc_p9_kotlin

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.example.oc_p9_kotlin.databinding.ActivityMainBinding
import com.example.oc_p9_kotlin.fakeapi.FakeEstateApi
import com.example.oc_p9_kotlin.models.Estate
import com.example.oc_p9_kotlin.models.InternetCallback
import com.example.oc_p9_kotlin.utils.Utils
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var estateList = FakeEstateApi.getFakeEstateList()
    private var selectedEstate: Estate? = null

    companion object {
        private const val REQUEST_PERMISSIONS_REQUEST_CODE = 1
        private const val TAG: String = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        //Verifies internet connection
        initInternetChecker()

        initRecyclerView()


        //TODO : uncomment to display map
        requestMapPermissions()

/*        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)


 */
        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        Log.d(TAG, "onCreate")

    }

    private fun initInternetChecker() {
        Utils().isInternetAvailable(this,object: InternetCallback {
            override fun onInternetEnabled() {
                Log.d(TAG, "onInternetEnabled")
            }

            override fun onInternetConfigChanged() {
                Log.d(TAG, "onInternetConfigChanged")
            }

            override fun onInternetDisabled() {
                Log.d(TAG, "onInternetDisabled")
            }

        })
    }

    private fun requestMapPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val permissionsToRequest: ArrayList<String?> = ArrayList()
        for (i in grantResults.indices) {
            permissionsToRequest.add(permissions[i])
        }
        if (permissionsToRequest.size > 0) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toArray(arrayOfNulls(0)),
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
    }


    private fun initRecyclerView() {
        binding.listRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.listRecyclerView.adapter = EstateAdapter(
            estateList
        ) {
            //if fragmentDetail present, tablet
            //else mobile
            onEstateClick(it)
        }

    }

    fun onEstateClick(estate: Estate) {
        selectedEstate = estate
        Log.d(TAG, estate.type.toString())
        Log.d(TAG, binding.slidingPaneLayout.isOpen.toString() + " ")
        openDetails()

    }

    fun openDetails() {
        // A method on the Fragment that owns the SlidingPaneLayout,
        // called by the adapter when an item is selected.
        supportFragmentManager.commit {
            //setReorderingAllowed(true)
            val fragment = DetailsFragment()
            fragment.arguments = bundleOf("estate" to selectedEstate)

            replace(
                R.id.detail_container,
                fragment
            )
            // If we're already open and the detail pane is visible,
            // crossfade between the fragments.
            if (binding.slidingPaneLayout.isOpen) {
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            }
        }
        binding.slidingPaneLayout.open()
    }

/*
    override fun onBackPressed() {

        Log.d(TAG, "isOpen : " + binding.slidingPaneLayout.isOpen)
        Log.d(TAG, "isEnabled : " + binding.slidingPaneLayout.isEnabled)
        Log.d(TAG, "isDetailVisible : " + binding.detailContainer.isVisible)
        Log.d(TAG, "isListVisible : " + binding.listRecyclerView.isVisible)

        finish()


        if(binding.slidingPaneLayout.isOpen) {
            Log.d(TAG, "slidingPaneLayout isOpen")
            binding.slidingPaneLayout.closePane()

        } else {
            finish()
            Log.d(TAG, "finish")
        }


*/


/*
        if(binding.slidingPaneLayout.isOpen && !binding.listRecyclerView.isVisible) {
            Log.d(TAG, "open, list not visible")
            binding.slidingPaneLayout.closePane()

        } else {
            finish()
            Log.d(TAG, "finish")

        }



    }
    */


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

/*
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

 */
}