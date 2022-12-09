package com.dicoding.smartbin.ui.message

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dicoding.smartbin.R
import com.dicoding.smartbin.databinding.ActivityHomeBinding
import com.dicoding.smartbin.databinding.FragmentMessageBinding
import com.dicoding.smartbin.modelsfactory.ViewModelFactory
import com.dicoding.smartbin.ui.home.HomeFragment
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*


class MessageFragment : Fragment() {

    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeActivityBinding: ActivityHomeBinding

    private val messageViewModel: MessageViewModel by viewModels { ViewModelFactory.getInstance(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessageBinding.inflate(inflater,container, false)
        homeActivityBinding = ActivityHomeBinding.inflate(layoutInflater)

        val sp = context!!.getSharedPreferences("SmartBin", Context.MODE_PRIVATE)
        val savedDateTime = sp.getString("CLICK_DATE", "")
        if ("" == savedDateTime) {
            sp.edit().putBoolean("REQUEST_STATE", false)
            binding.btnRequest.isEnabled = true
            binding.btnRequest.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.light_green))
        } else {
            val dateStringNow = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(Calendar.getInstance().time)
            //compare savedDateTime with today's datetime (dateStringNow), and act accordingly
            if (savedDateTime == dateStringNow){
                binding.btnRequest.isEnabled = false
                binding.btnRequest.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.grey))
            }else{
                sp.edit().putBoolean("REQUEST_STATE", false)
                binding.btnRequest.isEnabled = true
                binding.btnRequest.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.light_green))
            }

        }

        setupView()

        return binding.root
    }

    private fun setupView() {
        binding.btnRequest.setOnClickListener {
            val dateString: String =
                SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(Calendar.getInstance().time)
            val sp = context!!.getSharedPreferences("SmartBin", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sp.edit()
            editor.putString("CLICK_DATE", dateString)
            editor.putBoolean("REQUEST_STATE", true)
            editor.commit()

            val message = binding.messageBox.text.toString()
            composeEmail(message)

            //set fragment to homeactivity
            val fragment = HomeFragment()
            val fragmentManager = activity?.supportFragmentManager
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.nav_host_fragment_activity_home,fragment)
            fragmentTransaction?.commit()

            val navView = findNavController()
            navView.navigate(R.id.navigation_home)

        }
    }

    private fun composeEmail(message: String) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.type = TYPE
        messageViewModel.getUser().observe(requireActivity()){
            val text = """
                Nama : ${it.name}
                Komplek : ${it.komplek}
                Blok : ${it.Blok}
                No. Rumah : ${it.noRumah}
                Pesan : $message
            """.trimIndent()
            intent.data = Uri.parse("mailto:$ADDRESS?subject=$SUBJECT&Body=$text")
        }
        startActivity(intent)
    }

    private fun composeWA(message: String){
        messageViewModel.getUser().observe(requireActivity()){
            val text = """
                Nama : ${it.name}
                Komplek : ${it.komplek}
                Blok : ${it.Blok}
                No. Rumah : ${it.noRumah}
                Pesan : $message
            """.trimIndent()
            val uri = "https://api.whatsapp.com/send?phone=${Phone}"+"&text=" + URLEncoder.encode(text, "UTF-8")
            val i =  Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(uri)
            startActivity(i)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val Phone = "6282215760138"
        private const val ADDRESS = "aliefmabdillah09@gmail.com"
        private const val SUBJECT = "REQUEST JEMPUT"
        private const val TYPE = "text/plain"
    }
}