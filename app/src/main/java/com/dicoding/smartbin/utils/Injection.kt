package com.dicoding.smartbin.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.dicoding.smartbin.data.api.ApiConfig
import com.dicoding.smartbin.data.local.UserPreference
import com.dicoding.smartbin.data.repository.DataRepository
import com.dicoding.smartbin.data.repository.UserRepository

object Injection {
    fun provideDataRepository(): DataRepository {
        val apiService = ApiConfig.getApiService()
        return DataRepository.getInstance(apiService)
    }

    fun provideUserRepository(dataStore: DataStore<Preferences>): UserRepository {
        val apiService = ApiConfig.getApiService()
        val pref = UserPreference.getInstance(dataStore)
        return UserRepository.getInstance(apiService, pref)
    }
}