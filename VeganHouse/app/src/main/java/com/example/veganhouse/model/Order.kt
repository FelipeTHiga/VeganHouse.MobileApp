package com.example.veganhouse.model

data class Order(
    val idOrder: Int,
    val user: User,
    val adress: String,
    val total: Double,
    val orderItems: List<CartItem>,
    val orderDate: String?,
    val orderStatus: String
)