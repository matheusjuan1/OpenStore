package com.mjtech.store.domain.payment.repository

import com.mjtech.store.domain.payment.model.Payment

interface PaymentRepository {

    /**
     * @param payment O objeto Payment contendo os detalhes do pagamento.
     * @param callback O callback para notificar o resultado do pagamento.
     */
    fun processPayment(
        payment: Payment,
        callback: PaymentCallback
    )
}