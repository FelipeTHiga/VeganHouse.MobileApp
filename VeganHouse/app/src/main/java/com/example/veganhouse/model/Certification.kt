package com.example.veganhouse.model

import retrofit2.http.Url

data class Certification(
    val name: String,
    val url: Url,
    val idCertification: Int
)
