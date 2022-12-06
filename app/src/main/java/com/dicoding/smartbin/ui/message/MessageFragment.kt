package com.dicoding.smartbin.ui.message

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dicoding.smartbin.R
import com.dicoding.smartbin.databinding.FragmentMessageBinding

class MessageFragment : Fragment() {

    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        _binding = FragmentMessageBinding.inflate(inflater,container, false)

        setupView()

        return binding.root
    }

    private fun setupView() {
        binding.btnRequest.setOnClickListener {
            // Do nothing.
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}