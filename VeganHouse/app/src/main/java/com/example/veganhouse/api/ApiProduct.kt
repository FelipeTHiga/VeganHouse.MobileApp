package com.example.veganhouse

import com.example.veganhouse.data.Product
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiProduct {

    @GET("products/all")
    fun getAllProducts() : Call<List<Product>>

    @GET("products/tag/{category}")
    fun getProductByCategory(@Path("category") category:String) : Call<List<Product>>

    @GET("products/filter/{filter}/{category}")
    fun getProductOrderBy(@Path("filter") filter:String, @Path("category") category:String) : Call<List<Product>>

    companion object {
        //var BASE_URL = "http://10.0.2.2:8080/" // IP "Coringa", identifica o localhost do Notebook em que esta conectado
        var BASE_URL = "http://174.129.13.249:8080/"

        fun criar() : ApiProduct {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ApiProduct::class.java)
        }
    }

}