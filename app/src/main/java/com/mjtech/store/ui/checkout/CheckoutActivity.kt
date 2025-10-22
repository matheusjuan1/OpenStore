package com.mjtech.store.ui.checkout

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mjtech.store.R
import com.mjtech.store.databinding.ActivityCheckoutBinding
import com.mjtech.store.domain.payment.entities.PaymentType
import com.mjtech.store.ui.cart.CartViewModel
import com.mjtech.store.ui.common.components.LoadingDialog
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckoutBinding
    private val checkoutViewModel: CheckoutViewModel by viewModel()
    private val cartViewModel: CartViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        initObservers()
        initListeners()
    }

    private fun initViews() {
        binding.appBar.flCartIconContainer.visibility = View.INVISIBLE
    }

    private fun initObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.cartUiState
                    .map { it.totalPrice }
                    .collect { totalPrice ->
                        checkoutViewModel.setTransactionAmount(totalPrice)
                    }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                checkoutViewModel.uiState
                    .map { it.isLoading }
                    .collect { isLoading ->
                        binding.layoutPaymentMethods.isEnabled = !isLoading
                        if (isLoading) {
                            LoadingDialog.show(supportFragmentManager)
                        } else {
                            LoadingDialog.hide(supportFragmentManager)
                        }
                    }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                checkoutViewModel.uiState
                    .map { it.paymentStatusMessage to it.navigateBack }
                    .collect { (paymentStatusMessage, navigateBack) ->
                        if (paymentStatusMessage != null) {
                            Toast.makeText(
                                this@CheckoutActivity,
                                paymentStatusMessage,
                                Toast.LENGTH_LONG
                            ).show()
                            checkoutViewModel.resetPaymentStatus()
                        }

                        if (navigateBack) {
                            finish()
                            checkoutViewModel.resetNavigation()
                        }
                    }
            }
        }
    }

    private fun initListeners() {
        binding.btnDebit.setOnClickListener {
            checkoutViewModel.startNewTransaction(PaymentType.DEBIT)
            checkoutViewModel.processPayment()
        }
        binding.btnCredit.setOnClickListener {
            if (checkoutViewModel.isInstallmentAvailable()) {
                checkoutViewModel.startNewTransaction(PaymentType.CREDIT)
                showInstallmentFragment()
            } else {
                checkoutViewModel.startNewTransaction(PaymentType.CREDIT)
                checkoutViewModel.processPayment()
            }
        }
        binding.btnPix.setOnClickListener {
            checkoutViewModel.startNewTransaction(PaymentType.PIX)
            checkoutViewModel.processPayment()
        }
        binding.btnVoucher.setOnClickListener {
            checkoutViewModel.startNewTransaction(PaymentType.VOUCHER)
            checkoutViewModel.processPayment()
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun showInstallmentFragment() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.fragment_container_view, InstallmentFragment())
            addToBackStack("installment_selection")
        }
    }
}