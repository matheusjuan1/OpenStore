package br.com.projetoacbr.food.view.dialog

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import br.com.projetoacbr.food.databinding.DialogCartSummaryBinding
import br.com.projetoacbr.food.view.activity.CheckoutActivity
import br.com.projetoacbr.food.viewModel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CartBottomSheetDialog : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "CartBottomSheet"
    }

    private lateinit var binding: DialogCartSummaryBinding
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCartSummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("DefaultLocale", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.cartTotalValue.observe(viewLifecycleOwner) { totalValue ->
            binding.tvCartTotalValue.text = "R$ ${String.format("%.2f", totalValue)}"
        }


        homeViewModel.totalCartItemsCount.observe(viewLifecycleOwner) { count ->
            if (count <= 0) {
                if (isAdded) {
                    dismissAllowingStateLoss()
                }
            }
        }

        binding.btnPayCart.setOnClickListener {
            startActivity(Intent(requireContext(), CheckoutActivity::class.java))
            dismissAllowingStateLoss()
        }
    }
}