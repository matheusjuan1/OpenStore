package com.mjtech.store.ui.checkout

import com.mjtech.store.domain.payment.entities.Payment

data class CheckoutUiState(
    val transactionAmount: Double = 0.0,
    val payment: Payment? = null,
    val installmentOptions: List<Int> = emptyList(),
    val paymentStatusMessage: String? = null,
    val navigateBack: Boolean = false,
    val isLoading: Boolean = true
)