package com.mjtech.store.ui.checkout

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mjtech.store.data.local.repository.LocalCartRepository

class CheckoutViewModel(application: Application) : AndroidViewModel(application) {

//    private val acquirerSdk = AcquirerSdkProvider.get()
//    private val paymentProcessor: PaymentProcessor = acquirerSdk.paymentProcessor

//    private val _payment = MutableLiveData<Payment>()
//    val payment: LiveData<Payment> get() = _payment

    private val _paymentStatus = MutableLiveData<String>()
    val paymentStatus: LiveData<String> get() = _paymentStatus

    private val _navigateBack = MutableLiveData<Boolean>()
    val navigateBack: LiveData<Boolean> get() = _navigateBack

    val installmentOptions = listOf(1, 2, 3, 4, 5, 6)

//    private val paymentCallback = object : PaymentCallback {
//        override fun onSuccess(transactionId: String, message: String?) {
//            _paymentStatus.value =
//                "$transactionId - ${message ?: "Pagamento realizado com sucesso!"}"
//            CartRepository.clearCart()
//            _navigateBack.value = true
//        }
//
//        override fun onFailure(errorCode: String, errorMessage: String) {
//            _paymentStatus.value = "Erro: $errorCode - $errorMessage"
//        }
//
//        override fun onCancelled(message: String?) {
//            _paymentStatus.value = message ?: ""
//        }
//    }

//    fun startNewPayment(paymentType: PaymentType) {
//        val orderId = System.currentTimeMillis()
//
//        _payment.value = Payment(
//            id = orderId,
//            amount = amountInCents(getTotal()),
//            type = paymentType,
//            installmentDetails = null
//        )
//    }

    fun onInstallmentSelected(installments: Int) {
//        _payment.value = _payment.value?.copy(
//            installmentDetails = InstallmentDetails(
//                installments = installments,
//                installmentType = InstallmentType.MERCHANT
//            )
//        )
    }

    fun processPayment() {
//        if (getTotal() <= 0) {
//            _paymentStatus.value = "Carrinho vazio! Adicione itens para pagar."
//            return
//        }
//
//        val currentPayment = _payment.value
//        if (currentPayment == null) {
//            _paymentStatus.value = "Ocorreu um erro ao processar o pagamento."
//            return
//        }
//
//        paymentProcessor.processPayment(currentPayment, paymentCallback)
    }

    fun getTotal(): Double {
        return LocalCartRepository.cartTotalValue.value ?: 0.0
    }

    fun getInstallmentValue(installment: Int): Double {
        return getTotal() / installment
    }

    /**
     * Função que retorna se o parcelamento está disponível.
     */
    fun isInstallmentAvailable(): Boolean {
        return getTotal() > 100.00
    }

    /**
     * Método auxiliar para converter o valor em reais para centavos.
     * @param amount Valor em reais.
     * @return Valor em centavos.
     */
    private fun amountInCents(amount: Double): Int {
        return (amount * 100).toInt()
    }
}