package com.dicoding.smartbin.ui.register

import androidx.lifecycle.ViewModel
import com.dicoding.smartbin.data.repository.DataRepository
import com.dicoding.smartbin.data.repository.UserRepository

class RegisterViewModel(private val dataRepo: DataRepository, private val userRepo: UserRepository) : ViewModel() {

    //fungsi mengambil data komplek
    fun getListKomplek() = dataRepo.getListKomplek()

    //fungsi register user
    fun register(
        name: String,
        komplek: String,
        noRumah: String,
        email: String,
        password: String
    ) = userRepo.registerUser(name, komplek, noRumah, email, password)
}