package com.mjtech.store.ui.checkout

import com.mjtech.store.domain.payment.entities.Payment

data class CheckoutUiState(
    val transactionAmount: Double = 0.0,
    val payment: Payment? = null,
    val installmentOptions: List<Int> = emptyList(),
    val paymentResult: Int? = null,
    val errorMessage: String? = null,
    val isLoading: Boolean = true
)