package com.example.veganhouse.service

import com.example.veganhouse.model.User
import com.example.veganhouse.model.UserRegister
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("users")
    fun resgiterUser(@Body newUser: UserRegister) : Call<User>

    companion object {

        var BASE_URL = "http://174.129.13.249:8080/"

        fun getInstance () : UserService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(UserService::class.java)
        }
    }
}