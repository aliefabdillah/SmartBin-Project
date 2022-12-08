package com.dicoding.smartbin.utils

import com.dicoding.smartbin.data.api.ApiConfig
import com.dicoding.smartbin.data.repository.DataRepository
import com.dicoding.smartbin.data.repository.UserRepository
import com.google.android.gms.common.api.Api

object Injection {
    fun provideDataRepository(): DataRepository {
        val apiService = ApiConfig.getApiService()
        return DataRepository.getInstance(apiService)
    }

    fun provideUserRepository(): UserRepository {
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService)
    }
}