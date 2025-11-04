package com.mjtech.store.ui.scale

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.mjtech.store.R

class CheckoutScaleDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.atencao))
            .setMessage(getString(R.string.message_scale_checkout))

            .setPositiveButton(getString(R.string.adicionar)) { _, _ ->
                (activity as? ScaleActivity)?.onAddMoreClick()
            }

            .setNegativeButton(getString(R.string.finalizar)) { _, _ ->
                (activity as? ScaleActivity)?.onFinishPlateClick()
            }
            .create()
    }

    override fun onStart() {
        super.onStart()
        dialog?.setCanceledOnTouchOutside(false)
        isCancelable = false
    }
}