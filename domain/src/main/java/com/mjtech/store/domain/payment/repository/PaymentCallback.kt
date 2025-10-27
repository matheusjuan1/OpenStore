package com.mjtech.store.domain.payment.repository

/**
 * Interface que define os métodos de callback para o processamento de pagamentos.
 */
interface PaymentCallback {
    fun onSuccess(transactionId: String, message: String? = null)
    fun onFailure(errorCode: String, errorMessage: String)
    fun onCancelled(message: String? = null)
}