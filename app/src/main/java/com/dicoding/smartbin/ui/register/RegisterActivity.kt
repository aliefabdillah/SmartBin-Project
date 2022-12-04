package com.dicoding.smartbin.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.dicoding.smartbin.R
import com.dicoding.smartbin.databinding.ActivityRegisterBinding
import com.dicoding.smartbin.ui.HomeActivity

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnRegister.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_register -> {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }
        }

    }
}