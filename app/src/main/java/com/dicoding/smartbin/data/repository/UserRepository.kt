package com.dicoding.smartbin.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.dicoding.smartbin.data.api.ApiService
import com.dicoding.smartbin.data.api.RegisterRespone
import com.dicoding.smartbin.data.Result
import com.dicoding.smartbin.data.api.LoginResponse
import com.dicoding.smartbin.data.api.UsersItem
import com.dicoding.smartbin.data.local.UserModel
import com.dicoding.smartbin.data.local.UserPreference
import com.dicoding.smartbin.utils.EventHandlerToast
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPref: UserPreference
){
    private val registerResult = MediatorLiveData<Result<RegisterRespone>>()
    private val loginResult = MediatorLiveData<Result<UsersItem>>()

    fun registerUser(name: String, komplek: String, noRumah: String, email: String, password: String): LiveData<Result<RegisterRespone>>{
        registerResult.value = Result.Loading
        val client = apiService.register(name, komplek, noRumah, email, password)

        client.enqueue(object : retrofit2.Callback<RegisterRespone>{
            override fun onResponse(
                call: Call<RegisterRespone>,
                response: Response<RegisterRespone>
            ) {
                if (response.isSuccessful){
                    val responseData = response.body()!!
                    if (responseData.error == "200"){
                        registerResult.value = Result.Success(responseData)
                    }else{
                        registerResult.value = Result.Error(EventHandlerToast(responseData.message))
                        Log.e(TAG, "error in onResponse Method: ${responseData.message}")
                    }
                }else{
                    val jsonObject = JSONObject(response.errorBody()!!.string())
                    val errorMessage = jsonObject.getString("message")
                    registerResult.value = Result.Error(EventHandlerToast(errorMessage))
                    Log.e(TAG, "unsuccessfull response in onResponse Method: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RegisterRespone>, t: Throwable) {
                registerResult.value = Result.Error(EventHandlerToast(t.message.toString()))
                Log.e(TAG, "OnFailure in Failure Method: ${t.message}")
            }
        })

        return registerResult
    }

    fun loginUser(email: String, password: String): LiveData<Result<UsersItem>>{
        loginResult.value = Result.Loading
        val client = apiService.login(email, password)

        client.enqueue(object : retrofit2.Callback<LoginResponse>{
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful){
                    val responseData = response.body()!!
                    if (responseData.error == "200"){
                        val loginData = UsersItem(
                            responseData.user.name,
                            responseData.user.id,
                            responseData.user.namaKomplek,
                            responseData.user.noRumah
                        )
                        loginResult.value = Result.Success(loginData)
                    }else{
                        loginResult.value = Result.Error(EventHandlerToast(responseData.message))
                        Log.e(TAG, "error in onResponse Method: ${responseData.message}")
                    }
                }else{
                    val jsonObject = JSONObject(response.errorBody()!!.string())
                    val errorMessage = jsonObject.getString("message")
                    loginResult.value = Result.Error(EventHandlerToast(errorMessage))
                    Log.e(TAG, "unsuccessfull response in onResponse Method: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                loginResult.value = Result.Error(EventHandlerToast(t.message.toString()))
                Log.e(TAG, "OnFailure in Failure Method: ${t.message}")
            }
        })

        return loginResult
    }

    suspend fun saveUser(user: UserModel){
        userPref.saveUser(user)
    }

    fun getUser() = userPref.getUser()

    suspend fun deleteUser(){
        userPref.logout()
    }

    companion object {
        private const val TAG = "UserRepository"

        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService, userPref: UserPreference
        ): UserRepository =
            instance ?: synchronized(this){
                instance ?: UserRepository(apiService, userPref)
            }.also { instance = it }
    }
}