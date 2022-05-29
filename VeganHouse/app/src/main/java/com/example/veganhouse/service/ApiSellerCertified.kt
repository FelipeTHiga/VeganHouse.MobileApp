package com.example.veganhouse.api

import com.example.veganhouse.model.Certification
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiSellerCertified {

    @GET("certifieds/{fkSeller}")
    fun getSellerCertified(@Path("fkSeller") fkSeller:Int) : Call<List<Certification>>

    companion object {
        var BASE_URL = "http://52.207.48.19:8080/"

        fun criar() : ApiSellerCertified {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ApiSellerCertified::class.java)
        }
    }
}