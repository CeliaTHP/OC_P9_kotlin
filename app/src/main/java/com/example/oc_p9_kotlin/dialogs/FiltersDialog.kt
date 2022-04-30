package com.example.oc_p9_kotlin.dialogs

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import com.example.oc_p9_kotlin.databinding.FiltersDialogBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object FiltersDialog {
    private const val TAG = " FiltersDialog"


    fun showDialog(context: Context, onConfirmClick: () -> Unit): AlertDialog {

        val binding = FiltersDialogBinding.inflate(LayoutInflater.from(context))

        val dialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .create()

        binding.filtersConfirmButton.setOnClickListener {
            Log.d(TAG, "onConfirmation")
        }

        return dialog
    }

}