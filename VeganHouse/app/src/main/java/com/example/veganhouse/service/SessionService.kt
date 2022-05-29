package com.example.veganhouse.service

import android.content.res.Resources
import com.example.veganhouse.R
import com.example.veganhouse.model.User
import com.example.veganhouse.model.UserLogin
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface SessionService {
    @POST("session/login")
    fun postLogin(@Body user: UserLogin) : Call<User>


        companion object {
            var BASE_URL = "http://174.129.13.249:8080/"

        fun getInstace () : SessionService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(SessionService::class.java)
        }
    }
}