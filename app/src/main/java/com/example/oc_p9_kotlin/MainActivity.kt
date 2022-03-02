package com.example.oc_p9_kotlin

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oc_p9_kotlin.databinding.ActivityMainBinding
import com.example.oc_p9_kotlin.fakeapi.FakeEstateApi
import com.example.oc_p9_kotlin.models.Estate
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var estateList = FakeEstateApi.getFakeEstateList()

    companion object {
        private const val TAG: String = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        initRecyclerView()


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

    private fun initRecyclerView() {

        Log.d(TAG, estateList.toString())
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

        if(!binding.slidingPaneLayout.isOpen) {
            openDetails(estate)
        } else {
            //updateDetails(estate)
        }
    }

    fun openDetails(estate: Estate) {
        // A method on the Fragment that owns the SlidingPaneLayout,
        // called by the adapter when an item is selected.
        supportFragmentManager.commit {
            //setReorderingAllowed(true)
            val fragment = DetailsFragment()
            fragment.arguments = bundleOf("estate" to estate)

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