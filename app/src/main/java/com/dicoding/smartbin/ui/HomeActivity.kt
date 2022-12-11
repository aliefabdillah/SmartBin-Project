package com.dicoding.smartbin.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dicoding.smartbin.R
import com.dicoding.smartbin.databinding.ActivityHomeBinding
import com.dicoding.smartbin.modelsfactory.ViewModelFactory
import com.dicoding.smartbin.ui.home.HomeViewModel
import com.dicoding.smartbin.ui.login.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val homeViewModel: HomeViewModel by viewModels { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#0F3D3E")))
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_home)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home, R.id.navigation_message, R.id.navigation_about))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        setupData()
    }

    private fun setupData() {
        homeViewModel.getUser().observe(this){ data ->
            if (!data.isLogin) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menuLogout -> {
                // viewModel.logout()
                AlertDialog.Builder(this).apply {
                    setTitle(R.string.logout_title)
                    setMessage(R.string.logout_message)
                    setPositiveButton(getString(R.string.logout)) { _, _ ->
                        homeViewModel.logout()
                    }
                    setNegativeButton(R.string.cancel) { dialog, _ ->
                        dialog.cancel()
                    }
                    create()
                    show()
                }
            }
        }
        return true
    }

}