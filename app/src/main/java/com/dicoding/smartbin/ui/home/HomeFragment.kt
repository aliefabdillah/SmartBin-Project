package com.dicoding.smartbin.ui.home

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.dicoding.smartbin.data.local.UserModel
import com.dicoding.smartbin.databinding.FragmentHomeBinding
import com.dicoding.smartbin.modelsfactory.ViewModelFactory
import com.dicoding.smartbin.utils.ForegroundService
import com.firebase.client.DataSnapshot
import com.firebase.client.Firebase
import com.firebase.client.FirebaseError
import com.firebase.client.ValueEventListener
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val homeViewModel: HomeViewModel by viewModels { ViewModelFactory.getInstance(requireActivity())}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        Firebase.setAndroidContext(requireActivity())

        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.getUser().observe(viewLifecycleOwner){ user ->
                renderView(user)
            }
        }

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.getUser().observe(viewLifecycleOwner){ user ->
                renderView(user)
            }
        }
    }

    private fun renderView(user: UserModel) {

        with(binding){
            tvNamaValue.text = user.name
            tvKomplekValue.text = user.komplek
            tvBlokValue.text = "${user.Blok} / No. ${user.noRumah}"

            val mRef = Firebase("https://smartbin-35eab-default-rtdb.firebaseio.com/")
            val data = mRef.child(user.komplek).child(user.Blok).child(user.noRumah)
            val sp = context!!.getSharedPreferences("SmartBin", Context.MODE_PRIVATE)

            if (user.isLogin){
                data.addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()){
                            val volume = (dataSnapshot.value as Long).toInt()

                            tvVolumePercent.text = "$volume%"
                            ObjectAnimator.ofInt(binding.progressBar, "progress", volume)
                                .setDuration(2000)
                                .start()

                            if(volume == 100){
                                val requestState = sp.getBoolean("REQUEST_STATE", false)

                                val activity = activity
                                if (activity != null && isAdded){
                                    val intent = Intent(requireActivity(), ForegroundService::class.java)
                                    intent.putExtra("name", user.name)

                                    if (!requestState){
                                        CHECK = true

                                        if (Build.VERSION.SDK_INT >= 26) {
                                            requireActivity().startForegroundService(intent)
                                        } else {
                                            requireActivity().startService(intent)
                                        }
                                    }else{
                                        if (CHECK){
                                            requireActivity().stopService(intent)
                                            CHECK = false
                                        }
                                    }
                                }
                            }
                        }else{
                            val dataRef = dataSnapshot.ref
                            dataRef.setValue(0)
                            tvVolumePercent.text = "0%"
                            ObjectAnimator.ofInt(binding.progressBar, "progress", 0)
                                .setDuration(2000)
                                .start()
                        }
                    }

                    override fun onCancelled(p0: FirebaseError?) {

                    }

                })
            }
        }
    }

    companion object{
        private var CHECK = false
    }

}