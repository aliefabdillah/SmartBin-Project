package com.dicoding.smartbin.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.smartbin.data.local.UserModel
import com.dicoding.smartbin.data.repository.UserRepository

class HomeViewModel(private val userRepo: UserRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    fun getUser(): LiveData<UserModel> {
        return userRepo.getUser().asLiveData()
    }
}