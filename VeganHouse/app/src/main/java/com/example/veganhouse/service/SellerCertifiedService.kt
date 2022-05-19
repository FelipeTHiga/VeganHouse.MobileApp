package com.example.veganhouse.api

import com.example.veganhouse.model.Certification
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface SellerCertifiedService {

    @GET("certifieds/{fkSeller}")
    fun getSellerCertified(@Path("fkSeller") fkSeller:Int) : Call<ArrayList<Certification>>

    companion object {
        var BASE_URL = "http://174.129.13.249:8080/"

        fun criar() : SellerCertifiedService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(SellerCertifiedService::class.java)
        }
    }
}