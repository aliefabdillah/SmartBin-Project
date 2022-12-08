package com.dicoding.smartbin.ui.login

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.smartbin.R
import com.dicoding.smartbin.databinding.ActivityLoginBinding
import com.dicoding.smartbin.modelsfactory.ViewModelFactory
import com.dicoding.smartbin.ui.register.RegisterActivity
import com.dicoding.smartbin.data.Result
import com.dicoding.smartbin.data.local.UserModel
import com.dicoding.smartbin.ui.HomeActivity

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding

    private val loginViewModel: LoginViewModel by viewModels { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupView()
    }

    private fun setupView() {
        binding.btnLogin.setOnClickListener(this)
        binding.registerText.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_login -> {
                val email = binding.edLoginEmail.text.toString()
                val pass = binding.edLoginPassword.text.toString()
                val intent = Intent(this, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
//                signInCallback(email, pass)
            }
            R.id.register_text -> {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun signInCallback(email: String, pass: String) {
        loginViewModel.loginUser(email, pass).observe(this){ result ->
            if (result != null){
                when(result){
                    is Result.Loading -> binding.loadingIcon.visibility = View.VISIBLE
                    is Result.Success -> {
                        binding.loadingIcon.visibility = View.GONE
                        val username = result.data.name
                        val arrayText: List<String> = result.data.namaKomplek.split(" - ")
                        val komplek = arrayText[0]
                        val blok = arrayText[1]
                        val nomor = result.data.noRumah

                        loginViewModel.saveUser(UserModel(username, komplek, blok, nomor, true))
                        AlertDialog.Builder(this).apply {
                            setMessage(getString(R.string.login_success))
                            setPositiveButton(getString(R.string.positive_button_text)) { _, _ ->
                                val intent = Intent(context, HomeActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            }
                            create()
                            show()
                        }
                    }
                    is Result.Error -> {
                        binding.loadingIcon.visibility = View.GONE
                        result.error.getContentIfNotHandled()?.let {
                            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}