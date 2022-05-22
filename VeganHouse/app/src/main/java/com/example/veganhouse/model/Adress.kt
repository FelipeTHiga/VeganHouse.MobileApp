package com.example.veganhouse.model

data class Adress(
    var idAdress: Int?,
    var street: String,
    var number: String,
    var state: String,
    var city: String,
    var complement: String?,
    var cep: String,
    var district: String,
    var fkUser: Int?
) {
    //constructor(street: String, number: String, state: String, city: String, complement: String, cep: String, district: Int, fkUser: Int) : this()
}
