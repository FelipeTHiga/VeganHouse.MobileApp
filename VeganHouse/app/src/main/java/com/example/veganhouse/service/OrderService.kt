package com.example.veganhouse.service

import com.example.veganhouse.model.Order
import com.example.veganhouse.model.User
import retrofit2.Call
import retrofit2.http.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface OrderService {

    @GET("orders/user/{idUser}")
    fun getUserOrder(@Path("idUser") idUser: Int) : Call<List<Order>>

    @POST("orders")
    fun createOrder(@Body user: User) : Call<Void>

    @GET("orders/checkout/lastOrder/{idUser}")
    fun getOrder(@Path("idUser") idUser: Int) : Call<Order>

    @PUT("orders/checkout/orderItens/{idUser}")
    fun updateOrder(@Path("idUser") idUser: Int) : Call<Void>

    @PATCH("orders/update-status/{newStatus}/{idOrder}")
    fun pacthOrder(@Path("newStatus") newStatus: String, @Path("idOrder") idOrder : Int ) : Call<Void>

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