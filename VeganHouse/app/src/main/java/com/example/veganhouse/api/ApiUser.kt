package com.example.veganhouse.api

import com.example.veganhouse.data.User
import com.example.veganhouse.data.UserRegister
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiUser {
    @POST("users")
    fun resgiterUser(@Body newUser: UserRegister) : Call<User>

    companion object {
        var BASE_URL = "http://10.0.2.2:8080/"

        fun criar () : ApiUser {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ApiUser::class.java)
        }
    }
}