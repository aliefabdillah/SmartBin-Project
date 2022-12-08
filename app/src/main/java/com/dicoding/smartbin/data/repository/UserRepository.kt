package com.dicoding.smartbin.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.dicoding.smartbin.data.api.ApiService
import com.dicoding.smartbin.data.api.RegisterRespone
import com.dicoding.smartbin.data.Result
import com.dicoding.smartbin.utils.EventHandlerToast
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class UserRepository private constructor(
    private val apiService: ApiService
){
    private val registerResult = MediatorLiveData<Result<RegisterRespone>>()

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

    companion object {
        private const val TAG = "UserRepository"

        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this){
                instance ?: UserRepository(apiService)
            }.also { instance = it }
    }
}