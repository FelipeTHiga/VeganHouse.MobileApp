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
        // 10.0.2.2 ip coringa para testar com a API local
        // 52.207.48.19 ip da máquina na AWS que está com o backend
        //var BASE_URL = "http://10.0.2.2:8080/"
        var BASE_URL = "http://174.129.13.249:8080/"
        //var BASE_URL = "http://10.0.2.2:8080/"

        fun criar () : ApiLogin {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ApiLogin::class.java)
        }
    }
}