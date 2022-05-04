package com.example.veganhouse.model

data class Product(
    val id: Int,
    var name: String,
    var price: Double,
    var category: String,
    var description: String,
    var inventory: Int,
    var fkSeller: Int,
//    var image_url1: ByteArray,
//    var image_url2: ByteArray,
//    var image_url3: ByteArray,
    var image_url1: String,
    var image_url2: String,
    var image_url3: String,
    var isAvailable: Boolean
)

