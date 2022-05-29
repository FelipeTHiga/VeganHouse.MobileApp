package com.example.veganhouse.api

import com.example.veganhouse.model.Certification
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface SellerCertifiedService {

    @GET("certifieds/mobile/{fkSeller}")
    fun getSellerCertified(@Path("fkSeller") fkSeller:Int) : Call<List<Certification>>

    companion object {
        var BASE_URL = "http://52.207.48.19:8080/"

        fun getInstance() : SellerCertifiedService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(SellerCertifiedService::class.java)
        }
    }
}