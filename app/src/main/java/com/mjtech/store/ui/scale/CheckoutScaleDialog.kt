package com.mjtech.store.ui.scale

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class CheckoutScaleDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Deseja adicionar mais itens a sua compra?")

            .setPositiveButton("Adicionar") { _, _ ->
                (activity as? ScaleActivity)?.onAddMoreClick()
            }

            .setNegativeButton("Finalizar") { _, _ ->

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