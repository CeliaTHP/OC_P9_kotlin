package org.savecode.eiver.pages.account.parameters.view.dialog

import android.app.AlertDialog
import android.content.Context
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.example.oc_p9_kotlin.R
import com.example.oc_p9_kotlin.databinding.AuthenticationDialogBinding


object AuthDialog {

    fun showDialog(
        context: Context,
        onConfirm: (isCreating: Boolean, email: String, password: String) -> Unit,
        onResetPasswordClick: (email: String) -> Unit
    ): AlertDialog {
        val TAG = "AuthDialog"

        var isCreating = false

        val binding =
            AuthenticationDialogBinding.inflate(LayoutInflater.from(context), null, false)

        val popup = AlertDialog.Builder(context)
            .setView(binding.root)
            .setCancelable(false)
            .create()

        popup.show()
        popup.window?.setBackgroundDrawableResource(android.R.color.transparent)

        binding.authCreateAccount.setOnClickListener {
            if (!isCreating) {
                val createAccountText = SpannableString(context.getString(R.string.auth_already_account))
                createAccountText.setSpan(UnderlineSpan(), 0, createAccountText.length, 0)
                binding.authCreateAccount.text = createAccountText

                binding.authResetPassword.visibility = View.GONE

                binding.authConfirmButton.text = context.getString(R.string.auth_register)
                isCreating = true
            } else {
                val createAccountText = SpannableString(context.getString(R.string.auth_create_account))
                createAccountText.setSpan(UnderlineSpan(), 0, createAccountText.length, 0)
                binding.authCreateAccount.text = createAccountText
                binding.authResetPassword.visibility = View.VISIBLE
                binding.authConfirmButton.text = context.getString(R.string.auth_log_in)

                isCreating = false
            }
        }

        binding.authResetPassword.setOnClickListener {
            if (binding.authIdentifierInput.editText?.text.isNullOrBlank()) {
                binding.authIdentifierInput.error =
                    context.getString(R.string.add_estate_input_error)
            } else {
                binding.authIdentifierInput.error =
                    null
                onResetPasswordClick(binding.authIdentifierInput.editText?.text.toString())
            }
        }

        popup.setOnDismissListener {
            Log.d(TAG, "onDismiss")
        }



        binding.authConfirmButton.setOnClickListener {
            if (binding.authIdentifierInput.editText?.text.isNullOrBlank() ||
                binding.authPasswordInput.editText?.text.isNullOrBlank()
            ) {
                if (binding.authIdentifierInput.editText?.text.isNullOrBlank())
                    binding.authIdentifierInput.error =
                        context.getString(R.string.add_estate_input_error)
                else
                    binding.authIdentifierInput.error = null

                if (binding.authPasswordInput.editText?.text.isNullOrBlank())
                    binding.authPasswordInput.error =
                        context.getString(R.string.add_estate_input_error)
                else
                    binding.authPasswordInput.error = null

            } else {
                binding.authIdentifierInput.error = null
                binding.authPasswordInput.error = null
                onConfirm(
                    isCreating,
                    binding.authIdentifierInput.editText?.text.toString(),
                    binding.authPasswordInput.editText?.text.toString()
                )
            }

            //  if (popup.isShowing)
            //    popup.dismiss()
        }
        return popup

    }

}