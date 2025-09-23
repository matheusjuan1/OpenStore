package com.mjtech.pos.ui.checkout

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mjtech.pos.R
import com.mjtech.pos.databinding.FragmentInstallmentBinding

/**
 * Fragmento responsável por exibir e gerenciar a seleção de parcelamento.
 */
class InstallmentFragment : Fragment() {

    private val checkoutViewModel: CheckoutViewModel by activityViewModels()
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

    @SuppressLint("DefaultLocale")
    private fun configView() {
        val installmentOptions = checkoutViewModel.installmentOptions

        /* Cria os botões de parcelamento dinâmicamente de acordo com os tipos de parcelamento
         *  disponíves na ViewModel
         */
        installmentOptions.forEach { installment ->
            val button = Button(context, null, 0, R.style.AcbrButtonInstallments).apply {
                text =
                    if (installment == 1) "À Vista: R$${
                        String.format(
                            "%.2f",
                            checkoutViewModel.getTotal()
                        )
                    }"
                    else "${installment}x de R$${
                        String.format(
                            "%.2f",
                            checkoutViewModel.getInstallmentValue(installment)
                        )
                    }"
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).also {
                    it.setMargins(0, 0, 0, 16)
                }
                setOnClickListener {
                    checkoutViewModel.onInstallmentSelected(installment)
                    parentFragmentManager.popBackStack()
                }
            }
            binding.lytInstallmentButtons.addView(button)
        }
    }
}