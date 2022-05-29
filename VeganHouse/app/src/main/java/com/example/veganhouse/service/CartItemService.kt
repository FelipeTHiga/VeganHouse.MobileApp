package com.example.veganhouse.service

import com.example.veganhouse.model.CartItem
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface CartItemService {

    @GET("cartItems/{idUser}")
    fun getUserCartItems(@Path("idUser") idUser: Int) : Call<List<CartItem>>

    @POST("cartItems/{idUser}")
    fun addCartItem(@Path("idUser") idUser: Int, @Body cartItem: CartItem) : Call<CartItem>

    @DELETE("cartItems/{idCartItem}")
    fun removeCartItem(@Path("idCartItem") idCartItem: Int) : Call<CartItem>

    @PATCH("cartItems/increment/{idCartItem}")
    fun incrementCartItemQuantity(@Path("idCartItem") idCartItem: Int) : Call<CartItem>

    @PATCH("cartItems/decrement/{idCartItem}")
    fun decrementCartItemQuantity(@Path("idCartItem") idCartItem: Int) : Call<CartItem>

    companion object {
        // 10.0.2.2 ip coringa para testar com a API local
        // 52.207.48.19 ip da máquina na AWS que está com o backend
        //var BASE_URL = "http://10.0.2.2:8080/"
        var BASE_URL = "http://52.207.48.19:8080/"

        fun getInstance () : CartItemService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(CartItemService::class.java)
        }
    }
}