package com.example.veganhouse.service

import com.example.veganhouse.model.Adress
import com.example.veganhouse.model.User
import com.example.veganhouse.model.UserRegister
import com.example.veganhouse.model.UserUpdate
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface UserService {

    @GET("users/{idUser}")
    fun getUserById(@Path("idUser") idUser:Int) : Call<User>

    @POST("users")
    fun resgiterUser(@Body newUser: UserRegister) : Call<User>

    @PUT("users")
    fun putUser(@Body newUpdate: User) : Call<User>

    @GET("users/adress/{idUser}")
    fun getUserAdress(@Path("idUser") idUser: Int) : Call<Adress>

    @POST("users/adress")
    fun createUserAdress(@Body newAdress : Adress) : Call<Void>

    @PUT("users/adress/{idAdress}")
    fun putUserAdress(@Path("idAdress") idAdress: Int, @Body adressUpdate : Adress) : Call<Void>

    companion object {

//        var BASE_URL = "http://174.129.13.249:8080/"
          var BASE_URL = "https://veganhouseback.ddns.net/"

        fun getInstance () : UserService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(UserService::class.java)
        }
    }
}