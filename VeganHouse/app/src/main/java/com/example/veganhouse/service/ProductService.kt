package com.example.veganhouse

import com.example.veganhouse.model.Product
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ProductService {

    @GET("products/all")
    fun getAllProducts() : Call<List<Product>>

    @GET("products/tag/{category}")
    fun getProductByCategory(@Path("category") category:String) : Call<List<Product>>

    @GET("products/filter/{filter}/{category}")
    fun getProductOrderBy(@Path("filter") filter:String, @Path("category") category:String) : Call<List<Product>>

    @GET("products/{id}")
    fun getProductById(@Path("id") id:Int) : Call<Product>

    @GET("products/name/{name}")
    fun getProductByName(@Path("name") name:String) : Call<List<Product>>

    @GET("products/last-new-products")
    fun getNewProducts() : Call<List<Product>>

    @GET("products/featured-product")
    fun getFeaturedProduct() : Call<Product>


    companion object {
        var BASE_URL = "http://174.129.13.249:8080/"

        fun getInstance() : ProductService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ProductService::class.java)
        }
    }

}