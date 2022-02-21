package com.example.oc_p9_kotlin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.oc_p9_kotlin.databinding.FragmentDetailsBinding
import com.example.oc_p9_kotlin.databinding.FragmentSplitBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SplitFragment : Fragment() {

    companion object {
        private const val TAG: String = "SplitFragment"
    }

    private var _binding: FragmentSplitBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSplitBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onViewCreated")

        binding.buttonSplit.setOnClickListener {
            Log.d(TAG, "onClick SplitFragment")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}