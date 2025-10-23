package com.mjtech.store.ui.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.mjtech.store.R
import com.mjtech.store.databinding.FragmentInstallmentBinding
import com.mjtech.store.ui.common.currencyFormat
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class InstallmentFragment : Fragment() {

    private val checkoutViewModel: CheckoutViewModel by activityViewModel()
    private lateinit var binding: FragmentInstallmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInstallmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configView()
    }

    private fun configView() {
        val installmentOptions = checkoutViewModel.uiState.value.installmentOptions

        binding.lytInstallmentButtons.removeAllViews()

        installmentOptions.forEach { installment ->
            val button = Button(context, null, 0, R.style.StoreButtonInstallments).apply {

                val transactionAmount = checkoutViewModel.uiState.value.transactionAmount
                text =
                    if (installment == 1) "À Vista: ${
                        transactionAmount.currencyFormat()
                    }"
                    else "${installment}x de ${
                        checkoutViewModel.getInstallmentValue(installment).currencyFormat()
                    }"
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).also {
                    it.setMargins(0, 0, 0, 16)
                }
                setOnClickListener {
                    checkoutViewModel.onInstallmentSelected(installment)
                    checkoutViewModel.processPayment()
                    parentFragmentManager.popBackStack()
                }
            }
            binding.lytInstallmentButtons.addView(button)
        }

        binding.btnCancel.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}