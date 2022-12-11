package com.dicoding.smartbin.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference private constructor(private val dataStore: DataStore<Preferences>){

    fun getUser(): Flow<UserModel> {
        return dataStore.data.map { preference ->
            UserModel(
                preference[NAME_KEY] ?: "",
                preference[KOMPLEK_KEY] ?: "",
                preference[BLOK_KEY] ?: "",
                preference[NOMOR_KEY] ?: "",
                preference[STATE_KEY] ?: false
            )
        }
    }

    suspend fun saveUser(user: UserModel){
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = user.name
            preferences[KOMPLEK_KEY] = user.komplek
            preferences[BLOK_KEY] = user.Blok
            preferences[NOMOR_KEY] = user.noRumah
            preferences[STATE_KEY] = user.isLogin
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = ""
            preferences[KOMPLEK_KEY] = ""
            preferences[BLOK_KEY] = ""
            preferences[NOMOR_KEY] = ""
            preferences[STATE_KEY] = false
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val NAME_KEY = stringPreferencesKey("name")
        private val KOMPLEK_KEY = stringPreferencesKey("komplek")
        private val BLOK_KEY = stringPreferencesKey("blok")
        private val NOMOR_KEY = stringPreferencesKey("nomor")
        private val STATE_KEY = booleanPreferencesKey("state")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this){
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}