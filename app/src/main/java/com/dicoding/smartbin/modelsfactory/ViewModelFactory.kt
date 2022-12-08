package com.dicoding.smartbin.modelsfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.smartbin.data.repository.DataRepository
import com.dicoding.smartbin.data.repository.UserRepository
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
            else -> throw IllegalArgumentException("Uknown ViewModel Class: " + modelClass.name)
        }
    }

    companion object {

        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this){
                instance ?: ViewModelFactory(
                    Injection.provideDataRepository(),
                    Injection.provideUserRepository()
                )
            }.also { instance = it }
    }
}