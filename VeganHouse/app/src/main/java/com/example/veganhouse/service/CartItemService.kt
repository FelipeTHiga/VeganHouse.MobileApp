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
    fun addCartItem(@Path("idUser") idUser: Int, @Body cartItem: CartItem) : Call<Void>

    @DELETE("cartItems/{idCartItem}")
    fun removeCartItem(@Path("idCartItem") idCartItem: Int?) : Call<Void>

    @PATCH("cartItems/increment/{idCartItem}")
    fun incrementCartItemQuantity(@Path("idCartItem") idCartItem: Int?) : Call<Void>

    @PATCH("cartItems/decrement/{idCartItem}")
    fun decrementCartItemQuantity(@Path("idCartItem") idCartItem: Int?) : Call<Void>

    companion object {

//        var BASE_URL = "http://174.129.13.249:8080/"
        var BASE_URL = "https://veganhouseback.ddns.net/"

        fun getInstance () : CartItemService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(CartItemService::class.java)
        }
    }
}