package com.example.veganhouse.model

data class Adress(
    var idAdress: Int,
    var street: String,
    var number: Int,
    var state: String,
    var city: String,
    var complement: String,
    var cep: String,
    var district: String,
    var fkUser: Int
)
