package com.mjtech.store.ui.checkout

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mjtech.store.domain.payment.entities.InstallmentDetails
import com.mjtech.store.domain.payment.entities.InstallmentType
import com.mjtech.store.domain.payment.entities.Payment
import com.mjtech.store.domain.payment.entities.PaymentType
import com.mjtech.store.domain.payment.usecases.PaymentCallback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CheckoutViewModel() : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

    private val paymentCallback = object : PaymentCallback {
        override fun onSuccess(transactionId: String, message: String?) {
            _uiState.update {
                it.copy(
                    paymentStatusMessage = "$transactionId - ${message ?: "Pagamento realizado com sucesso!"}",
                    navigateBack = true,
                    isLoading = false
                )
            }
        }

        override fun onFailure(errorCode: String, errorMessage: String) {
            _uiState.update {
                it.copy(
                    paymentStatusMessage = "Erro: $errorCode - $errorMessage",
                    isLoading = false
                )
            }
        }

        override fun onCancelled(message: String?) {
            _uiState.update {
                it.copy(
                    paymentStatusMessage = message ?: "Pagamento cancelado pelo usuário.",
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
                        paymentStatusMessage = "Carrinho vazio! Adicione itens para pagar."
                    )
                }
                return
            }

            val currentPayment = payment
            if (currentPayment == null) {
                _uiState.update {
                    it.copy(
                        paymentStatusMessage = "Ocorreu um erro ao processar o pagamento."
                    )
                }
                return
            }

            Log.d("CheckoutViewModel", "Processing payment: $currentPayment")
//            paymentProcessor.processPayment(currentPayment, paymentCallback)
        }
    }

    fun resetPaymentStatus() {
        _uiState.update { it.copy(paymentStatusMessage = null) }
    }

    fun resetNavigation() {
        _uiState.update { it.copy(navigateBack = false) }
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
}