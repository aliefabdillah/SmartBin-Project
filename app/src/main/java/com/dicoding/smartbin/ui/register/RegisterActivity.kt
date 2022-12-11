package com.dicoding.smartbin.ui.register

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.smartbin.R
import com.dicoding.smartbin.data.Result
import com.dicoding.smartbin.databinding.ActivityRegisterBinding
import com.dicoding.smartbin.modelsfactory.ViewModelFactory
import com.dicoding.smartbin.utils.EventHandlerToast

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityRegisterBinding

    private val registerViewModel: RegisterViewModel by viewModels { ViewModelFactory.getInstance(this) }

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

        setupViewKomplek()
    }

    private fun setupViewKomplek(){
        //get data komplek
        registerViewModel.getListKomplek().observe(this){ result ->
            if (result != null){
                when(result){
                    is Result.Loading -> binding.loadingIcon.visibility = View.VISIBLE
                    is Result.Success -> {
                        binding.loadingIcon.visibility = View.GONE

                        val listKomplek = arrayListOf<String>()
                        result.data.forEach { data ->
                            listKomplek.add(data.komplekNama +" - "+data.blok)
                        }

                        val komplekAdapter = ArrayAdapter(this, R.layout.dropdown_item, listKomplek)
                        binding.autoCompleteKomplek.setAdapter(komplekAdapter)
                    }
                    is Result.Error -> {
                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.loadingIcon.visibility = View.GONE
                            result.error.getContentIfNotHandled()?.let { toastText ->
                                Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
                            }
                        }, TIME_OUT)
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_register -> {
                with(binding){
                    val name = edRegisterUsername.text.toString()
                    val email = edRegisterEmail.text.toString()
                    val pass = edRegisterPassword.text.toString()
                    val komplek = autoCompleteKomplek.text.toString()
                    val nomor = autoCompleteNoRumah.text.toString()

                    when {
                        komplek == getString(R.string.hint_komplek) -> autoCompleteKomplek.error = getString(R.string.error_empty)
                        nomor == getString(R.string.hint_nomor) -> autoCompleteNoRumah.error = getString(R.string.error_empty)
                        else -> registerCallback(name, email, pass, komplek, nomor)
                    }
                }
            }
        }
    }

    private fun registerCallback(
        name: String,
        email: String,
        pass: String,
        komplek: String,
        nomor: String
    ) {
        registerViewModel.register(name, komplek, nomor, email, pass).observe(this){ result ->
            if (result != null){
                when(result){
                    is Result.Loading -> binding.loadingIcon.visibility = View.VISIBLE
                    is Result.Success -> {
                        binding.loadingIcon.visibility = View.GONE
                        EventHandlerToast(result.data.message).getContentIfNotHandled()?.let { toastText ->
                            Toast.makeText(this, toastText, Toast.LENGTH_LONG).show()
                        }
                        finish()
                    }
                    is Result.Error -> {
                        binding.loadingIcon.visibility = View.GONE
                        result.error.getContentIfNotHandled()?.let { toastText ->
                            Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    companion object{
        const val TIME_OUT = 5000L
    }
}