package com.example.veganhouse.model

data class CartItem(
    val idCartItem: Int,
    val product: Product,
    val quantity: Int,
    val subTotal: Double,
    val fkUser: Int,
    val fkOrder: Int
)
