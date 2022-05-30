package com.example.veganhouse.api


import com.example.veganhouse.model.Certification
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiSellerCertified {

    @GET("certifieds/{fkSeller}")
    fun getSellerCertified(@Path("fkSeller") fkSeller:Int) : Call<List<Certification>>

    companion object {
//        var BASE_URL = "http://174.129.13.249:8080/"
          var BASE_URL = "http://10.0.2.2:8080/"

        fun criar() : ApiSellerCertified {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ApiSellerCertified::class.java)
        }
    }
}