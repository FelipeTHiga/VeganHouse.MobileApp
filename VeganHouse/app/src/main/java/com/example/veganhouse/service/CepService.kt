package com.example.veganhouse.service

import com.example.veganhouse.model.Cep
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface CepService {

    @GET("{cep}/json/")
    fun getCep(@Path("cep") cep: String): Call<Cep>

    companion object {

        var BASE_URL = "https://viacep.com.br/ws/"

        fun getInstance(): CepService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(CepService::class.java)
        }
    }

}