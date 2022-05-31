package com.example.veganhouse.model

data class CardPayment (
    val token: String,
    val issuerId: String,
    val paymentMethodId: String,
    val transactionAmount: Float,
    val installments: Int,
    val productDescription: String,
    val payer: Payer
    )