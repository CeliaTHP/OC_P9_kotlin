package com.example.oc_p9_kotlin.activities

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oc_p9_kotlin.*
import com.example.oc_p9_kotlin.adapters.EstateAdapter
import com.example.oc_p9_kotlin.databinding.ActivityMainBinding
import com.example.oc_p9_kotlin.events.OnEstateEvent
import com.example.oc_p9_kotlin.fragments.DetailsFragment
import com.example.oc_p9_kotlin.models.Estate
import com.example.oc_p9_kotlin.models.EstateType
import com.example.oc_p9_kotlin.utils.InternetUtils
import com.example.oc_p9_kotlin.view_models.MainViewModel
import io.reactivex.rxjava3.kotlin.addTo
import org.greenrobot.eventbus.EventBus

class MainActivity : CompositeDisposableActivity() {

    companion object {
        private const val REQUEST_PERMISSIONS_REQUEST_CODE = 1
        private const val TAG: String = "MainActivity"
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var estateAdapter: EstateAdapter

    private var estateList = mutableListOf<Estate>()

    private lateinit var mainViewModel: MainViewModel

    private var onEstateEvent = OnEstateEvent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.overflowIcon =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_filter, null)
        setSupportActionBar(binding.toolbar)

        //default estate

        initViewModels()

        initObservables()
        //initRecyclerView(estateList)
        //TODO : uncomment to display map
        requestMapPermissions()


/*        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

 */
        initListeners()


        Log.d(TAG, "onCreate")

    }

    private fun initListeners() {

        binding.fab.setOnClickListener {
            this.startActivity(Intent(this, AddEstateActivity::class.java))

        }


    }


    private fun initObservables() {

        mainViewModel.getAll()
            .subscribe(
                {
                    if (it.isNullOrEmpty()) {
                        //Generating data on our database
                        Log.d(TAG, "generateData")
                        mainViewModel.generateData()

                    } else {
                        //Updating our list with retrieved data
                        estateList = it
                        Log.d(TAG, "data already found : list = " + estateList.size)
                        initRecyclerView(estateList, null)

                        //Setting our first item as default selected estate
                        Log.d(TAG, "set default estate " + estateList[0])
                        onEstateEvent.setSelectedEstate(estateList[0])
                        EventBus.getDefault().postSticky(onEstateEvent)

                        for (estate in estateList) {
                            Log.d(
                                TAG, "location = " +
                                        estate.location.latitude.toString() + " - " + estate.location.longitude.toString()
                            )
                        }
                    }

                }, {

                }).addTo(bag)


    }

    private fun initViewModels() {

        mainViewModel =
            ViewModelProvider(this, MainViewModelFactory(this)).get(MainViewModel::class.java)

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

        /*
        if (permissionsToRequest.size > 0) {

            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toArray(arrayOfNulls(0)),
                REQUEST_PERMISSIONS_REQUEST_CODE
            )

    }
         */
    }


    private fun initRecyclerView(estateList: MutableList<Estate>, filter: EstateType?) {

        estateAdapter = EstateAdapter(
            estateList, filter
        ) {
            onEstateClick(it)
        }
        binding.listRecyclerView.adapter = estateAdapter
        binding.listRecyclerView.layoutManager = LinearLayoutManager(this)


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
            R.id.action_settings -> {

                Log.d(TAG, "SETTINGS")
                true
            }
            R.id.action_filter_all -> {

                estateAdapter.updateData(estateList)

                Log.d(TAG, "ALL")
                true
            }
            R.id.action_filter_house -> {

                estateAdapter.updateData(estateList.filter { it.type == EstateType.HOUSE } as MutableList<Estate>)

                Log.d(TAG, "HOUSE")
                true
            }
            R.id.action_filter_apartment -> {

                estateAdapter.updateData(estateList.filter { it.type == EstateType.APARTMENT } as MutableList<Estate>)

                Log.d(TAG, "APARTMENT")
                true
            }

            R.id.action_filter_building -> {

                estateAdapter.updateData(estateList.filter { it.type == EstateType.BUILDING } as MutableList<Estate>)

                //estateAdapter.setFilter(EstateType.BUILDING)

                Log.d(TAG, "BUILDING")
                true
            }

            R.id.action_filter_loft -> {

                estateAdapter.updateData(estateList.filter { it.type == EstateType.LOFT } as MutableList<Estate>)

                //estateAdapter.setFilter(EstateType.LOFT)

                Log.d(TAG, "LOFT")
                true
            }

            R.id.action_filter_castle -> {

                estateAdapter.updateData(estateList.filter { it.type == EstateType.CASTLE } as MutableList<Estate>)
                Log.d(TAG, "CASTLE")
                true
            }

            R.id.action_filter_boat -> {

                estateAdapter.updateData(estateList.filter { it.type == EstateType.BOAT } as MutableList<Estate>)

                Log.d(TAG, "BOAT")
                true
            }

            R.id.action_filter_mansion -> {

                estateAdapter.updateData(estateList.filter { it.type == EstateType.MANSION } as MutableList<Estate>)

                Log.d(TAG, "MANSION")
                true
            }
            R.id.action_filter_site -> {

                estateAdapter.updateData(estateList.filter { it.type == EstateType.SITE } as MutableList<Estate>)

                Log.d(TAG, "SITE")
                true
            }
            R.id.action_filter_other -> {

                estateAdapter.updateData(estateList.filter { it.type == EstateType.OTHER } as MutableList<Estate>)

                Log.d(TAG, "OTHER")
                true
            }

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