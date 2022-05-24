package com.example.veganhouse.model

data class User(
    val id: Int?,
    var nameUser: String?,
    var surName: String?,
    var cpf: String?,
    var email: String?,
    var passwordUser: String?,
    var isSeller: Boolean?,
    var authenticated: Boolean?,
    var adress: Adress?
)
