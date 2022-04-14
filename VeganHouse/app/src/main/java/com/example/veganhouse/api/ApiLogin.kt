package com.example.veganhouse.api

import com.example.veganhouse.data.User
import com.example.veganhouse.data.UserLogin
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiLogin {
    @POST("session/login")
    fun postLogin(@Body user: UserLogin) : Call<User>
    //local storage -
    companion object {
        var BASE_URL = "http://10.0.2.2:8080/"

        fun criar () : ApiLogin {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ApiLogin::class.java)
        }
    }
}