package com.dicoding.smartbin.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.dicoding.smartbin.data.Result
import com.dicoding.smartbin.data.api.ApiService
import com.dicoding.smartbin.data.api.KomplekResponse
import com.dicoding.smartbin.data.api.ListkomplekItem
import com.dicoding.smartbin.utils.EventHandlerToast
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class DataRepository private constructor(
    private val apiService: ApiService
){
    private val getListKomplek = MediatorLiveData<Result<List<ListkomplekItem>>>()

    fun getListKomplek(): LiveData<Result<List<ListkomplekItem>>>{
        getListKomplek.value = Result.Loading
        val client = apiService.getKomplek()

        client.enqueue(object : retrofit2.Callback<KomplekResponse>{
            override fun onResponse(
                call: Call<KomplekResponse>,
                response: Response<KomplekResponse>
            ) {
                if (response.isSuccessful){
                    val responseData = response.body()
                    if (responseData != null && responseData.error == "200"){
                        getListKomplek.value = Result.Success(responseData.listkomplek)
                    }else{
                        getListKomplek.value = Result.Error(EventHandlerToast(responseData!!.message))
                        Log.e(TAG, "error in response Method: ${responseData.message}")
                    }
                }else{
                    val jsonObject = JSONObject(response.errorBody()!!.string())
                    val errorMessage = EventHandlerToast(jsonObject.getString("message"))
                    getListKomplek.value = Result.Error(errorMessage)
                    Log.e(TAG, "Unsuccessfully Response Method: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<KomplekResponse>, t: Throwable) {
                getListKomplek.value = Result.Error(EventHandlerToast(t.message.toString()))
                Log.e(TAG, "OnFailure in Response Method: ${t.message}")
            }
        })

        return getListKomplek
    }


    companion object {
        private const val TAG = "DataRepository"

        @Volatile
        private var instance: DataRepository? = null
        fun getInstance(
            apiService: ApiService
        ): DataRepository =
            instance ?: synchronized(this){
                instance ?: DataRepository(apiService)
            }.also { instance = it }
    }
}