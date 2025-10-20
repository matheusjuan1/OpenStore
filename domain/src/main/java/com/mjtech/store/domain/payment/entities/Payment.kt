package com.mjtech.store.domain.payment.entities

import kotlinx.serialization.Serializable

/**
 * @param id O ID do pagamento.
 * @param amount O valor do pagamento, em centavos.
 * @param type O tipo de pagamento (crédito, débito, pix, etc.).
 * @param installmentDetails Detalhes do parcelamento, pode ser nulo.
 */
@Serializable
data class Payment(
    val id: Long,
    val amount: Long,
    val type: PaymentType,
    val installmentDetails: InstallmentDetails?
)