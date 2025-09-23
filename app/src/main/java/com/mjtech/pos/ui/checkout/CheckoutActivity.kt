package com.mjtech.pos.ui.checkout

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import com.mjtech.pos.R
import com.mjtech.pos.databinding.ActivityCheckoutBinding

class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckoutBinding

    private val checkoutViewModel: CheckoutViewModel by viewModels()

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
        binding.customAppBar.flCartIconContainer.visibility = View.INVISIBLE
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun initObservers() {
        checkoutViewModel.paymentStatus.observe(this) { status ->
            Toast.makeText(this, status, Toast.LENGTH_LONG).show()
        }

        checkoutViewModel.navigateBack.observe(this) { navigate ->
            if (navigate) {
                finish()
            }
        }

//        checkoutViewModel.payment.observe(this) { payment ->
//            if (payment.installmentDetails != null) {
//                checkoutViewModel.processPayment()
//            }
//        }
    }

    private fun initListeners() {
//        binding.btnDebit.setOnClickListener {
//            checkoutViewModel.startNewPayment(PaymentType.DEBIT)
//            checkoutViewModel.processPayment()
//        }
//        binding.btnCredit.setOnClickListener {
//            /*
//             * Exibe opções de parcelamento caso esteja disponível, caso contrário
//             * processa o pagamento como à vista.
//             */
//            if (checkoutViewModel.isInstallmentAvailable()) {
//                checkoutViewModel.startNewPayment(PaymentType.CREDIT)
//
//                showInstallmentFragment()
//            } else {
//                checkoutViewModel.startNewPayment(PaymentType.CREDIT)
//                checkoutViewModel.processPayment()
//            }
//        }
//        binding.btnPix.setOnClickListener {
//            checkoutViewModel.startNewPayment(PaymentType.PIX)
//            checkoutViewModel.processPayment()
//        }
//        binding.btnVoucher.setOnClickListener {
//            checkoutViewModel.startNewPayment(PaymentType.VOUCHER)
//            checkoutViewModel.processPayment()
//        }
//        binding.btnCancel.setOnClickListener {
//            finish()
//        }
    }

    /**
     * Exibe o fragmento de seleção de parcelamento.
     */
    private fun showInstallmentFragment() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.fragment_container_view, InstallmentFragment())
            addToBackStack("installment_selection")
        }
    }
}