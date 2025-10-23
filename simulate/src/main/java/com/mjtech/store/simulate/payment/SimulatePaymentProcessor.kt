package com.mjtech.store.simulate.payment

import com.mjtech.store.domain.payment.entities.Payment
import com.mjtech.store.domain.payment.usecases.PaymentCallback
import com.mjtech.store.domain.payment.usecases.PaymentProcessor

class SimulatePaymentProcessor : PaymentProcessor {
    override fun processPayment(
        payment: Payment,
        callback: PaymentCallback
    ) {
        if (payment.amount <= 100) {
            callback.onSuccess(
                "ID",
                "Simulated payment successful for amount: ${payment.amount}"
            )
        } else {
            callback.onFailure(
                "001",
                "Simulated payment failure for amount: ${payment.amount}"
            )
        }
    }
}