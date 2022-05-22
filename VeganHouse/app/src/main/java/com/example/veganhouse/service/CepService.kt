package com.example.veganhouse.service

import com.example.veganhouse.model.Cep
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface CepService {

    @GET("json/")
    fun getCep(): Call<Cep>

    class CepService(cep: String) {

        private val cep = cep

        fun getInstance(): CepService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://viacep.com.br/ws/${cep}/")
                .build()
            return retrofit.create(CepService::class.java)
        }

        //val cepService: CepService = getRetrofit().create(CepService::class.java)

    }

//    companion object {
//
//        val cep = "123"
//
//        var BASE_URL = "https://viacep.com.br/ws/${cep}/json/"
//
//        fun getInstance () : CepService {
//            val retrofit = Retrofit.Builder()
//                .addConverterFactory(GsonConverterFactory.create())
//                .baseUrl(BASE_URL)
//                .build()
//            return retrofit.create(CepService::class.java)
//        }
//    }
}