package com.dicoding.smartbin.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dicoding.smartbin.databinding.ActivityHomeBinding
import com.dicoding.smartbin.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {

    private lateinit var binding: FragmentAboutBinding
    private lateinit var homeActivityBinding: ActivityHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAboutBinding.inflate(inflater, container, false)
        homeActivityBinding = ActivityHomeBinding.inflate(layoutInflater)
        return binding.root
    }
}