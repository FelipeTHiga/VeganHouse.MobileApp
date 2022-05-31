package com.example.veganhouse.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface CartService {




    companion object {
//        var BASE_URL = "http://10.0.2.2:8080/"
        var BASE_URL = "https://veganhouseback.ddns.net/"

        fun getInstace () : OrderService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(OrderService::class.java)
        }
    }
}