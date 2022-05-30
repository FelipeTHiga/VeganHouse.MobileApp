package com.example.veganhouse.model
import java.time.LocalDate

class Order(
    val idOrder: Int,
    val user: User,
    val adress: String,
    var  total: Double,
    var orderItems: List<CartItem>,
    val orderDate: LocalDate,
    var orderStatus: String
)
