package com.dicoding.smartbin.ui.home

import androidx.lifecycle.*
import com.dicoding.smartbin.data.local.UserModel
import com.dicoding.smartbin.data.repository.UserRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val userRepo: UserRepository) : ViewModel() {

    fun getUser(): LiveData<UserModel> {
        return userRepo.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            userRepo.deleteUser()
        }
    }
}