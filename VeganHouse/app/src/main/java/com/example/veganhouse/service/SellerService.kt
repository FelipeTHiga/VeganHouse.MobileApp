package com.example.veganhouse.service

import com.example.veganhouse.model.Seller
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface SellerService {

    @GET("sellers/{idSeller}")
    fun getSellerById(@Path("idSeller") idSeller:Int) : Call<Seller>

    companion object {

//        var BASE_URL = "http://174.129.13.249:8080/"
          var BASE_URL = "https://veganhouseback.ddns.net/"

        fun getInstance() : SellerService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(SellerService::class.java)
        }
    }
}