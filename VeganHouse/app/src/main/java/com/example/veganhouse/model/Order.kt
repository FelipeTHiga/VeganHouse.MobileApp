package com.example.veganhouse.model

import java.time.LocalDate

data class Order(
    val idOrder: Int,
    val user: User,
    val adress: String,
    val total: Double,
    val orderItems: List<CartItem>,
    val orderDate: LocalDate?,
    val orderStatus: String
)