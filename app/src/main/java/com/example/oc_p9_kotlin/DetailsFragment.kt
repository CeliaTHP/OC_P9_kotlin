package com.example.oc_p9_kotlin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.oc_p9_kotlin.databinding.FragmentDetailsBinding
import com.example.oc_p9_kotlin.models.Estate

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class DetailsFragment : Fragment() {

    companion object {
        private const val TAG: String = "DetailsFragment"
    }

    private var _binding: FragmentDetailsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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