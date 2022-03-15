package com.example.oc_p9_kotlin.activities

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oc_p9_kotlin.*
import com.example.oc_p9_kotlin.adapters.EstateAdapter
import com.example.oc_p9_kotlin.databinding.ActivityMainBinding
import com.example.oc_p9_kotlin.events.OnEstateEvent
import com.example.oc_p9_kotlin.fakeapi.FakeEstateApi
import com.example.oc_p9_kotlin.fragments.DetailsFragment
import com.example.oc_p9_kotlin.models.Estate
import com.example.oc_p9_kotlin.utils.InternetUtils
import com.example.oc_p9_kotlin.utils.Utils
import com.example.oc_p9_kotlin.view_models.MainViewModel
import org.greenrobot.eventbus.EventBus

class MainActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_PERMISSIONS_REQUEST_CODE = 1
        private const val TAG: String = "MainActivity"
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var estateList = FakeEstateApi.getFakeEstateList()
    private var selectedEstate: Estate? = null
    private lateinit var mainViewModel: MainViewModel

    private var onEstateEvent = OnEstateEvent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        //default estate
        selectedEstate = estateList[0]




        initViewModels()
        //Verifies internet connection
        initRecyclerView()
        //TODO : uncomment to display map
        requestMapPermissions()


        onEstateEvent.setSelectedEstate(estateList[0])
        EventBus.getDefault().postSticky(onEstateEvent)

/*        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

 */
        binding.fab.setOnClickListener {
            mainViewModel

        }
        Log.d(TAG, "onCreate")

    }

    private fun initViewModels() {
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onStop() {
        super.onStop()

        InternetUtils.unregisterNetworkCallback(this)
    }

    override fun onStart() {
        super.onStart()
        InternetUtils.registerNetworkCallback(this)

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
        Log.d(TAG, estate.type.toString())
        Log.d(TAG, binding.slidingPaneLayout.isOpen.toString() + " ")

        onEstateEvent.setSelectedEstate(estate)
        EventBus.getDefault().postSticky(onEstateEvent)

        updateDetails()
    }


    fun updateDetails() {

        // A method on the Fragment that owns the SlidingPaneLayout,
        // called by the adapter when an item is selected.
        supportFragmentManager.commit {
            
            replace(
                R.id.detail_container,
                DetailsFragment()
            )
            // If we're already open and the detail pane is visible,
            // crossfade between the fragments.
            if (binding.slidingPaneLayout.isOpen) {
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            }
        }
        binding.slidingPaneLayout.open()
    }


    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        Log.d(TAG, "onKeyUp")
        handleBackButton()
        return super.onKeyUp(keyCode, event)

    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        Log.d(TAG, "dispatchKeyEvent")
        return super.dispatchKeyEvent(event)


    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Log.d(TAG, "onKeyDown")
        //handleBackButton()
        return super.onKeyDown(keyCode, event)


    }

    override fun onBackPressed() {

        //super.onBackPressed()
        Log.d(TAG, "onBackPressed")

        Log.d(TAG, "isOpen : " + binding.slidingPaneLayout.isOpen)
        Log.d(TAG, "isSlideable: " + binding.slidingPaneLayout.isSlideable)

        //if close & slideable ou open & pas sliedable close
        handleBackButton()


    }

    fun handleBackButton() {

        with(binding.slidingPaneLayout) {
            if (!isOpen && isSlideable || !isSlideable) {
                finish()

            } else {
                closePane()
                Log.d(TAG, "shouldClose")
            }
        }
    }

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