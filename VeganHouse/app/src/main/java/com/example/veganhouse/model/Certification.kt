package com.example.veganhouse.data

import retrofit2.http.Url

data class Certification(
    val name: String,
    val url: Url,
    val idCertification: Int
)
