package com.mjtech.store.domain.payment.usecases

import com.mjtech.store.domain.payment.entities.Payment

interface PaymentProcessor {

    /**
     * @param payment O objeto Payment contendo os detalhes do pagamento.
     * @param callback O callback para notificar o resultado do pagamento.
     */
    fun processPayment(
        payment: Payment,
        callback: PaymentCallback
    )
}