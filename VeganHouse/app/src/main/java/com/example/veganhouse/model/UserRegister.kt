package com.example.veganhouse.model

data class UserRegister(var nameUser: String,
                        var surName: String,
                        var cpf: String,
                        var email: String,
                        var passwordUser: String,
                        var isSeller: Boolean)
