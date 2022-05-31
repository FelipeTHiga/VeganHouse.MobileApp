package com.example.veganhouse.service

import com.example.veganhouse.model.Order
import retrofit2.Call
import retrofit2.http.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface OrderService {

    @GET("orders/user/{idUser}")
    fun getUserOrder(@Path("idUser") idUser: Int) : Call<List<Order>>

    companion object {
        var BASE_URL = "https://veganhouseback.ddns.net/"

        fun getInstance() : OrderService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(OrderService::class.java)
        }
    }
}