package com.dicoding.smartbin.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.smartbin.data.local.UserModel
import com.dicoding.smartbin.data.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepo: UserRepository) : ViewModel() {

    fun saveUser(user: UserModel){
        viewModelScope.launch {
            userRepo.saveUser(user)
        }
    }

    fun loginUser(email: String, pass: String) = userRepo.loginUser(email, pass)
}