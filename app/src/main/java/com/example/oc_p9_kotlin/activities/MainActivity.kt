package com.example.oc_p9_kotlin.activities

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.example.oc_p9_kotlin.*
import com.example.oc_p9_kotlin.adapters.EstateAdapter
import com.example.oc_p9_kotlin.databinding.ActivityMainBinding
import com.example.oc_p9_kotlin.events.OnEstateEvent
import com.example.oc_p9_kotlin.events.OnUpdateListEvent
import com.example.oc_p9_kotlin.fragments.DetailsFragment
import com.example.oc_p9_kotlin.models.Estate
import com.example.oc_p9_kotlin.models.EstateType
import com.example.oc_p9_kotlin.utils.InternetUtils
import com.example.oc_p9_kotlin.view_models.MainViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.internal.util.MergerBiFunction
import io.reactivex.rxjava3.kotlin.addTo
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class MainActivity : CompositeDisposableActivity() {

    companion object {
        private const val REQUEST_PERMISSIONS_REQUEST_CODE = 1
        private const val TAG: String = "MainActivity"

    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: EstateAdapter
    private var listBag = CompositeDisposable()

    private var estateList = mutableListOf<Estate>()
    //private var filteredList = mutableListOf<Estate>()

    private var isOverflowIconEnabled = false

    private lateinit var viewModel: MainViewModel

    private var onEstateEvent = OnEstateEvent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar()

        initRecyclerView(estateList)


        //default estate


        initViewModels()
        generateDataIfEmptyDb()

        getEstateList()
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

    @Subscribe(sticky = true)
    fun onUpdateListEvent(onUpdateListEvent: OnUpdateListEvent) {
        //     filteredList.clear()

        var filteredList = onUpdateListEvent.getFilteredEstateList()
        Log.d(TAG, "onUpdateList Event $filteredList")

        updateEstateList(filteredList, true)

    }

    private fun generateDataIfEmptyDb() {
        //Generating data on our database

        viewModel.getAll().firstOrError()
            .subscribe(
                {
                    if (it.isNullOrEmpty()) {
                        Log.d(TAG, "generateData")
                        viewModel.generateData()
                            .subscribe()
                            .addTo(bag)
                    }
                }, {
                    Log.d(TAG, "error getEstateList generation" + it.message)
                })
            .addTo(bag)
    }

    private fun initListeners() {

        binding.slidingPaneLayout.addPanelSlideListener(object :
            SlidingPaneLayout.PanelSlideListener {
            override fun onPanelSlide(panel: View, slideOffset: Float) {
            }

            override fun onPanelOpened(panel: View) {
                initToolbar()

            }

            override fun onPanelClosed(panel: View) {
                Log.d(TAG, "onPanelClose")

                initToolbar()
                stopPlayer()

            }

        })

        binding.newEstateFab.setOnClickListener {
            this.startActivity(Intent(this, AddEstateActivity::class.java))
        }
        binding.mapViewFab.setOnClickListener {

            this.startActivity(Intent(this, MapViewActivity::class.java))

        }


    }

    private fun stopPlayer() {
        Log.d(TAG, "stopPlayer")
        DetailsFragment.getPlayer()?.playWhenReady = false
        DetailsFragment.getPlayer()?.stop()

    }


    private fun getEstateList(estateType: EstateType? = null) {

        Log.d(TAG, "getEstateList")
        listBag.clear()

        if (estateType == null) {
            fetchAllEstateAndUpdateList()
        } else {
            fetchEstateOfTypeAndUpdateList(estateType)
        }

    }

    private fun fetchAllEstateAndUpdateList() {
        Log.d(TAG, "fetchAllEstate")


        Observable.merge(
            viewModel.getAll().take(1).map { Pair(it, true) },
            viewModel.getAll().skip(1).map { Pair(it, false) }
        )
            .subscribe({
                val estateList = it.first
                val isFirst = it.second
                if (!it.first.isNullOrEmpty()) {
                    Log.d(TAG, "list received getAll : " + it.toString())
                    updateEstateList(estateList, isFirst)
                } else {
                    updateEstateList(mutableListOf(), isFirst)
                }
            }, {
                Log.d(TAG, "error getEstateList getAll" + it.toString())
                Toast.makeText(this, R.string.data_error, Toast.LENGTH_LONG).show()

            }).addTo(listBag)
    }


    private fun fetchEstateOfTypeAndUpdateList(estateType: EstateType) {
        Log.d(TAG, "fetchWithTypeEstate")
        Observable.merge(
            viewModel.getByType(estateType).take(1).map { Pair(it, true) },
            viewModel.getByType(estateType).skip(1).map { Pair(it, false) }
        ).subscribe({
            val estateList = it.first
            val isFirst = it.second
            if (!estateList.isNullOrEmpty()) {
                Log.d(TAG, "list received getByType : " + it.toString())
                updateEstateList(estateList, isFirst)
            } else {
                updateEstateList(mutableListOf(), isFirst)
            }
        }, {
            Log.d(TAG, "error getEstateList getByType" + it.message)
            Toast.makeText(this, R.string.data_error, Toast.LENGTH_LONG).show()

        }).addTo(listBag)


    }


    private fun updateEstateList(estateList: MutableList<Estate>, isFirst: Boolean) {
        //Updating our list with retrieved data
        this.estateList = estateList
        adapter.updateData(estateList)
        if (isFirst)
            updateDefaultEstate(estateList)

    }


    private fun updateDefaultEstate(estateList: MutableList<Estate>) {
        //Setting our first item as default selected estate

        if (estateList.isNullOrEmpty())
            return

        Log.d(TAG, "set default estate " + estateList[0])
        onEstateEvent.setSelectedEstate(estateList[0])
        EventBus.getDefault().postSticky(onEstateEvent)
    }


    private fun initViewModels() {

        viewModel =
            ViewModelProvider(this, MainViewModelFactory(this)).get(MainViewModel::class.java)

    }


    override fun onStop() {
        super.onStop()
        InternetUtils.unregisterNetworkCallback(this)
        stopPlayer()

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


    private fun initRecyclerView(estateList: MutableList<Estate>) {

        adapter = EstateAdapter(
            estateList
        ) {
            onEstateClick(it)
        }
        binding.listRecyclerView.adapter = adapter

    }

    private fun onEstateClick(estate: Estate) {
        Log.d(TAG, estate.type.toString())
        Log.d(TAG, binding.slidingPaneLayout.isOpen.toString() + " ")

        onEstateEvent.setSelectedEstate(estate)
        EventBus.getDefault().postSticky(onEstateEvent)

        updateDetails()
    }


    private fun updateDetails() {

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
        with(binding.slidingPaneLayout) {
            if (!isOpen && isSlideable || !isSlideable) {
                finish()

            } else {
                closePane()
                Log.d(TAG, "shouldClose")
            }
        }
    }


    public fun initToolbar() {

        with(binding.slidingPaneLayout) {
            if (isOpen && isSlideable) {

                Log.d(TAG, "shouldHideIcon")
                isOverflowIconEnabled = false
                binding.toolbar.overflowIcon =
                    null

            } else {
                Log.d(TAG, "shouldNotHideIcon")
                isOverflowIconEnabled = true
                binding.toolbar.overflowIcon =
                    ResourcesCompat.getDrawable(resources, R.drawable.ic_filter, null)
                setSupportActionBar(binding.toolbar)


            }
        }

        setSupportActionBar(binding.toolbar)



    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        return if (isOverflowIconEnabled)
            super.onPrepareOptionsMenu(menu)
        else
            false
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)

        for (i in 0 until menu.size()) {
            val item = menu.getItem(i)
            val s = SpannableString(item.title)
            s.setSpan(
                ForegroundColorSpan(
                    ResourcesCompat.getColor(
                        resources,
                        R.color.main_text,
                        null
                    )
                ), 0, s.length, 0
            )
            item.title = s
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if (item.itemId == R.id.action_criteria) {
            Log.d(TAG, "criterias")

            val intent =
                Intent(binding.root.context, FiltersActivity::class.java)
            startActivity(intent)

            /*
            FiltersDialog.showDialog(this){

            }.show()
        */


        } else {
            when (item.itemId) {
                R.id.action_filter_all -> getEstateList()
                R.id.action_filter_house -> getEstateList(EstateType.HOUSE)
                R.id.action_filter_apartment -> getEstateList(EstateType.APARTMENT)
                R.id.action_filter_building -> getEstateList(EstateType.BUILDING)
                R.id.action_filter_loft -> getEstateList(EstateType.LOFT)
                R.id.action_filter_castle -> getEstateList(EstateType.CASTLE)
                R.id.action_filter_boat -> getEstateList(EstateType.BOAT)
                R.id.action_filter_mansion -> getEstateList(EstateType.MANSION)
                R.id.action_filter_site -> getEstateList(EstateType.SITE)
                R.id.action_filter_other -> getEstateList(EstateType.OTHER)

                else -> getEstateList()

            }

            //estateAdapter.updateData(filteredList)
            //updateDefaultEstate(filteredList)
        }



        return true


    }


    override fun onResume() {
        super.onResume()

        if (!EventBus.getDefault().isRegistered(this)) {
            Log.d(TAG, "register EventBus")
            EventBus.getDefault().register(this)
        }

    }

    override fun onPause() {
        super.onPause()

        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
            Log.d(TAG, "onPause unregister EventBus")
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