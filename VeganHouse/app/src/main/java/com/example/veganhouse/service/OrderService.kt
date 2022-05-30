package com.example.veganhouse.service
import com.example.veganhouse.model.Order
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.http.GET
import retrofit2.http.Path


interface OrderService {

    @GET("checkout/lastOrder/{idUser}")
    fun getLastUserOrder(@Path("idUser")idUser: Int): retrofit2.Call<Order>

    companion object {
        var BASE_URL = "http://10.0.2.2:8080/"

        fun getInstace () : OrderService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(OrderService::class.java)
        }
    }
}