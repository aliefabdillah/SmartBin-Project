package com.dicoding.smartbin.data.api

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    //endpoint register
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("nama_komplek") nama_komplek: String,
        @Field("no_rumah") no_rumah: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterRespone>

    //endpoint login
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ):Call<LoginResponse>

    //endpoint daftar Komplek
    @GET("listKomplek")
    fun getKomplek():Call<KomplekResponse>
}