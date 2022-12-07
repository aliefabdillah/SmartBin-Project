package com.dicoding.smartbin.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.dicoding.smartbin.R
import com.dicoding.smartbin.databinding.ActivityRegisterBinding
import com.dicoding.smartbin.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupView()
    }

    private fun setupView() {
        binding.btnRegister.setOnClickListener(this)

        val nomorRumah = resources.getStringArray(R.array.nomor_rumah)
        val adapter = ArrayAdapter(this, R.layout.dropdown_item, nomorRumah)
        binding.autoCompleteNoRumah.setAdapter(adapter)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_register -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }
}