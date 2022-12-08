package com.dicoding.smartbin.data.api

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    //endpoint register
    @FormUrlEncoded
    @POST("userAPI/register.php")
    fun register(
        @Field("name") name: String,
        @Field("nama_komplek") nama_komplek: String,
        @Field("no_rumah") no_rumah: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterRespone>

    //endpoint login
    @FormUrlEncoded
    @POST("userAPI/login.php")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ):Call<login>

    //endpoint daftar Komplek
    @GET("userAPI/listKomplek.php")
    fun getKomplek():Call<KomplekResponse>
}