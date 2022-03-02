package com.example.oc_p9_kotlin

import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.oc_p9_kotlin.databinding.FragmentDetailsBinding
import com.example.oc_p9_kotlin.models.Estate
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class DetailsFragment : Fragment() {

    companion object {
        private const val TAG: String = "DetailsFragment"
    }

    private var _binding: FragmentDetailsBinding? = null
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));

        _binding = FragmentDetailsBinding.inflate(inflater, container, false)

        return binding.root



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMap()
        val estate = arguments?.getParcelable<Estate>("estate")
        if (estate == null) {
            Log.d(TAG, "no estate")
        }
        //cancel detail
        else {
            updateUI(estate)
            Log.d(TAG, "onViewCreated + arguments : " + estate.type)
        }


        /* binding.detailsButton.setOnClickListener {

             //findNavController().navigate(R.id.action_DetailsFragment_to_ListFragment)
         }

         */
    }



    private fun initMap() {
    binding.map.setTileSource(TileSourceFactory.MAPNIK)
        val mapController: IMapController = binding.map.controller
        mapController.setZoom(9.5)
        val startPoint = GeoPoint(48.8583, 2.2944)
        mapController.setCenter(startPoint)

    }






    override fun onResume() {
        super.onResume()
        binding.map.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.map.onPause()
    }



    private fun updateUI(estate: Estate){

        binding.detailsType.text = estate.type.toString()
        binding.detailsDescriptionText.text = estate.description
        binding.detailsCity.text = estate.city
        binding.detailsSurface.text = getString(R.string.details_surface,estate.surfaceInSquareMeters)
        binding.detailsRooms.text = getString(R.string.details_rooms, estate.rooms.toString())
        binding.detailsBathrooms.text = getString(R.string.details_bathrooms, estate.bathrooms.toString())
        binding.detailsBedrooms.text = getString(R.string.details_bedrooms, estate.bedrooms.toString())


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}