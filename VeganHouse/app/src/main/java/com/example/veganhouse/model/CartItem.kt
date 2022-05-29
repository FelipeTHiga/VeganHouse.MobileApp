package com.example.veganhouse.model

data class CartItem (
    val idCartItem: Int,
    var product: Product,
    var quantity: Int,
    var subTotal: Double,
    var fkUser: Int,
    var fkOrder: Int

)