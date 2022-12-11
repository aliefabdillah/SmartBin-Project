package com.dicoding.smartbin.ui.message

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.smartbin.data.local.UserModel
import com.dicoding.smartbin.data.repository.UserRepository

class MessageViewModel(private val userRepo: UserRepository) : ViewModel() {
    fun getUser(): LiveData<UserModel> {
        return userRepo.getUser().asLiveData()
    }
}