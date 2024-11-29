package com.github.gabrielscremim.srbarber1

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.GET

interface ServicoAPI {
    @POST("login")
    fun fazerLogin(@Body corpo: Map<String, String>): Call<Map<String, String>>

    @POST("identified/saveData")
    @JvmSuppressWildcards
    fun salvarBarbeiro(
        @Header("Authorization") token: String,
        @Body corpo: Map<String, Any>
    ): Call<Void>

    @GET("identified/getData")
    fun buscarDados(@Header("Authorization") token: String): Call<List<Map<String, Any>>>
}