package com.example.veganhouse.model

data class Seller(
    var idSeller: Int,
    var cnpj: String,
    var commercialName: String,
    var commercialEmail: String,
    var whatsappNumber: String,
    var instagramAccount: String,
    var facebookAccount: String,
    var fkUser: Int,
)
