package com.mjtech.store.ui.checkout

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mjtech.store.domain.cart.repostitory.CartRepository
import com.mjtech.store.domain.common.Result
import com.mjtech.store.domain.payment.entities.InstallmentDetails
import com.mjtech.store.domain.payment.entities.InstallmentType
import com.mjtech.store.domain.payment.entities.Payment
import com.mjtech.store.domain.payment.entities.PaymentType
import com.mjtech.store.domain.payment.usecases.PaymentCallback
import com.mjtech.store.domain.payment.usecases.PaymentProcessor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CheckoutViewModel(
    private val paymentProcessor: PaymentProcessor,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

    private val paymentCallback = object : PaymentCallback {
        override fun onSuccess(transactionId: String, message: String?) {
            viewModelScope.launch {
                cartRepository.clearCart()
                    .collect { result ->
                        when (result) {
                            is Result.Success -> {
                                _uiState.update {
                                    it.copy(
                                        paymentResult = CheckoutActivity.RESULT_SUCCESS,
                                        isLoading = false
                                    )
                                }
                            }

                            is Result.Error -> {
                                Log.e(
                                    TAG,
                                    "Failed to clear cart after payment success: ${result.error}"
                                )
                                _uiState.update {
                                    it.copy(
                                        paymentResult = CheckoutActivity.RESULT_SUCCESS,
                                        isLoading = false
                                    )
                                }
                            }

                            is Result.Loading -> {}
                        }
                    }
            }
        }

        override fun onFailure(errorCode: String, errorMessage: String) {
            _uiState.update {
                it.copy(
                    paymentResult = CheckoutActivity.RESULT_RETRY,
                    errorMessage = "$errorCode: $errorMessage",
                    isLoading = false
                )
            }
        }

        override fun onCancelled(message: String?) {
            _uiState.update {
                it.copy(
                    paymentResult = CheckoutActivity.RESULT_RETRY,
                    errorMessage = message,
                    isLoading = false
                )
            }
        }
    }

    fun setTransactionAmount(amount: Double) {
        _uiState.update { it.copy(transactionAmount = amount) }
        getPaymentOptions(amount)
    }

    fun startNewTransaction(paymentType: PaymentType) {
        val orderId = System.currentTimeMillis()

        _uiState.update {
            it.copy(
                payment = Payment(
                    id = orderId,
                    amount = _uiState.value.transactionAmount,
                    type = paymentType,
                    installmentDetails = null
                )
            )
        }
    }

    fun onInstallmentSelected(installments: Int) {
        _uiState.update {
            it.copy(
                payment = it.payment?.copy(
                    installmentDetails = InstallmentDetails(
                        installments = installments,
                        installmentType = InstallmentType.MERCHANT
                    )
                )
            )
        }
    }

    fun processPayment() {
        _uiState.value.apply {
            if (transactionAmount <= 0) {
                _uiState.update {
                    it.copy(
                        paymentResult = CheckoutActivity.RESULT_FAILURE,
                        errorMessage = "Invalid transaction amount"
                    )
                }
                return
            }

            val currentPayment = payment
            if (currentPayment == null) {
                _uiState.update {
                    it.copy(
                        paymentResult = CheckoutActivity.RESULT_FAILURE,
                        errorMessage = "Payment information is missing"
                    )
                }
                return
            }

            paymentProcessor.processPayment(currentPayment, paymentCallback)
        }
    }

    fun resetPaymentResult() {
        _uiState.update { it.copy(paymentResult = null, errorMessage = null) }
    }

    fun isInstallmentAvailable(): Boolean {
        return _uiState.value.installmentOptions.size > 1
    }

    fun getInstallmentValue(installment: Int): Double {
        return _uiState.value.transactionAmount / installment
    }

    private fun getPaymentOptions(amount: Double) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true
                )
            }

            val installmentOptions = if (amount >= 100.0) {
                listOf(1, 2, 3, 4, 5, 6)
            } else {
                listOf(1)
            }
            _uiState.update {
                it.copy(
                    installmentOptions = installmentOptions,
                    isLoading = false
                )
            }
        }
    }

    companion object {
        private const val TAG = "CheckoutViewModel"
    }
}