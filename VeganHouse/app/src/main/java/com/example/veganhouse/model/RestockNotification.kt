package com.example.veganhouse.model

data class RestockNotification(
    var idRestockNotification: Int?,
    var fkUser: Int,
    var fkProduct: Int
)
