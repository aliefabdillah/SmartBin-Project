package com.dicoding.smartbin.modelsfactory

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.smartbin.data.repository.DataRepository
import com.dicoding.smartbin.data.repository.UserRepository
import com.dicoding.smartbin.ui.home.HomeViewModel
import com.dicoding.smartbin.ui.login.LoginViewModel
import com.dicoding.smartbin.ui.message.MessageViewModel
import com.dicoding.smartbin.ui.register.RegisterViewModel
import com.dicoding.smartbin.utils.Injection

class ViewModelFactory(
    private val dataRepository: DataRepository, private val userRepository: UserRepository
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(dataRepository, userRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(MessageViewModel::class.java) -> {
                MessageViewModel(userRepository) as T
            }
            else -> throw IllegalArgumentException("Uknown ViewModel Class: " + modelClass.name)
        }
    }

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this){
                instance ?: ViewModelFactory(
                    Injection.provideDataRepository(),
                    Injection.provideUserRepository(context.dataStore)
                )
            }.also { instance = it }
    }
}