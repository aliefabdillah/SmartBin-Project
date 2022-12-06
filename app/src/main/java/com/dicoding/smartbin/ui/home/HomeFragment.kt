package com.dicoding.smartbin.ui.home

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dicoding.smartbin.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupView()

        return binding.root
    }

    private fun setupView() {
        binding.tvNamaValue.text = ": Hilman Fauzi"
        binding.tvBlokValue.text = ": A1 / No. 245"

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