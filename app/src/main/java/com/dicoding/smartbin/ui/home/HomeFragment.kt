package com.dicoding.smartbin.ui.home

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.dicoding.smartbin.data.local.UserModel
import com.dicoding.smartbin.databinding.FragmentHomeBinding
import com.dicoding.smartbin.modelsfactory.ViewModelFactory
import com.dicoding.smartbin.ui.login.LoginActivity
import com.dicoding.smartbin.ui.login.LoginViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels { ViewModelFactory.getInstance(requireActivity())}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupView()

        return binding.root
    }

    private fun setupView() {

        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.getUser().observe(viewLifecycleOwner){ user ->
                binding.tvNamaValue.text = user.name
                binding.tvKomplekValue.text = user.komplek
                binding.tvBlokValue.text = "${user.Blok} / No. ${user.noRumah}"
            }
        }

        val currentVolume = 90 // data volume dari firebase
        binding.tvVolumePercent.text = "$currentVolume%"
        ObjectAnimator.ofInt(binding.progressBar, "progress", currentVolume)
            .setDuration(2000)
            .start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}