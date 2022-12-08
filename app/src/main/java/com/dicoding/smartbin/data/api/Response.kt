package com.dicoding.smartbin.data.api

import com.google.gson.annotations.SerializedName

data class RegisterRespone(
	@field:SerializedName("error")
	val error: String,

	@field:SerializedName("message")
	val message: String,
)

data class Response(

	@field:SerializedName("error")
	val error: String,

	@field:SerializedName("users")
	val users: List<UsersItem>
)

data class UsersItem(

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("nama_komplek")
	val namaKomplek: String,

	@field:SerializedName("no_rumah")
	val noRumah: String,

	@field:SerializedName("email")
	val email: String
)

data class login(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("nama_komplek")
	val namaKomplek: String,

	@field:SerializedName("no_rumah")
	val noRumah: String
)

data class KomplekResponse(

	@field:SerializedName("listkomplek")
	val listkomplek: List<ListkomplekItem>,

	@field:SerializedName("error")
	val error: String,

	@field:SerializedName("message")
	val message: String,
)

data class ListkomplekItem(

	@field:SerializedName("komplek_nama")
	val komplekNama: String,

	@field:SerializedName("blok")
	val blok: String,

	@field:SerializedName("id")
	val id: String
)
