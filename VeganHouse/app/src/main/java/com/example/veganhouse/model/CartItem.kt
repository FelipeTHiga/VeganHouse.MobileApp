package com.example.veganhouse.model
data class CartItem(
     val idCartItem: Int,
     val product: Product,
     var quantity: Int,
     var subTotal: Double,
     val fkUser: Int,
     val fkOrder: Int
     )
