package com.dicoding.smartbin.ui.home

import android.animation.ObjectAnimator
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.dicoding.smartbin.R
import com.dicoding.smartbin.data.local.UserModel
import com.dicoding.smartbin.databinding.FragmentHomeBinding
import com.dicoding.smartbin.modelsfactory.ViewModelFactory
import com.dicoding.smartbin.ui.HomeActivity
import com.dicoding.smartbin.utils.ForegroundService
import com.firebase.client.DataSnapshot
import com.firebase.client.Firebase
import com.firebase.client.FirebaseError
import com.firebase.client.ValueEventListener
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

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

    private fun renderView(user: UserModel) {

        with(binding){
            tvNamaValue.text = user.name
            tvKomplekValue.text = user.komplek
            tvBlokValue.text = "${user.Blok} / No. ${user.noRumah}"

            val mRef = Firebase("https://smartbin-35eab-default-rtdb.firebaseio.com/")
            val data = mRef.child(user.komplek).child(user.Blok).child(user.noRumah)

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
                                val sp = context?.getSharedPreferences("SmartBin", Context.MODE_PRIVATE)
                                val requestState = sp?.getBoolean("REQUEST_STATE", false)

                                val intent = Intent(context, ForegroundService::class.java)
                                intent.putExtra("name", user.name)
                                if (!requestState!!){
                                    if (Build.VERSION.SDK_INT >= 26) {
                                        activity?.startForegroundService(intent)
                                    } else {
                                        activity?.startService(intent)
                                    }
                                }else{
                                    activity?.stopService(intent)
                                }
                            }
                        }else{
                            val dataRef = dataSnapshot.ref
                            dataRef.setValue(0)
                        }
                    }

                    override fun onCancelled(p0: FirebaseError?) {

                    }

                })
            }
        }
    }

    private fun createNotification(name: String) {
        val requestId = System.currentTimeMillis()
        val intent = Intent(requireActivity(), HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            requireActivity(),
            requestId.toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE)

        val mNotifManager = activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(requireActivity(), CHANNEL_ID)
            .setSmallIcon(R.drawable.smart_bin_small)
            .setContentTitle(getString(R.string.notif_title, name))
            .setContentText(getString(R.string.notif_message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /* Create or update. */
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = CHANNEL_NAME
            builder.setChannelId(CHANNEL_ID)
            mNotifManager.createNotificationChannel(channel)
        }

        val notification = builder.build()
        mNotifManager.notify(NOTIFICATION_ID, notification)
    }

    private fun cancelNotification() {
        val mNotifManager = activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotifManager.cancel(NOTIFICATION_ID)
    }

    companion object{
        private const val CHANNEL_ID = "FULL_NOTIFICATION"
        private const val CHANNEL_NAME = "SmartBin Channel"
        private const val NOTIFICATION_ID = 1
    }

}