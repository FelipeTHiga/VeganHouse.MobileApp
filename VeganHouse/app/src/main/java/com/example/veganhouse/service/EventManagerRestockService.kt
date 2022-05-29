package com.example.veganhouse.service

import com.example.veganhouse.model.RestockNotification
import retrofit2.Retrofit
import retrofit2.Call
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface EventManagerRestockService {


    @POST("restock-subscribe")
    fun createSubscription(@Body restockNotification: RestockNotification): Call<RestockNotification>

    companion object {

        var BASE_URL = "http://174.129.13.249:8080/"

        fun getInstance() : EventManagerRestockService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(EventManagerRestockService::class.java)
        }
    }
}