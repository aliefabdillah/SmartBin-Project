package com.dicoding.smartbin.ui.message

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dicoding.smartbin.databinding.FragmentMessageBinding
import com.dicoding.smartbin.modelsfactory.ViewModelFactory
import java.net.URLEncoder

class MessageFragment : Fragment() {

    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!

    private val messageViewModel: MessageViewModel by viewModels { ViewModelFactory.getInstance(requireActivity()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        _binding = FragmentMessageBinding.inflate(inflater,container, false)

        setupView()

        return binding.root
    }

    private fun setupView() {
        binding.btnRequest.setOnClickListener {
            val message = binding.messageBox.text.toString()
//            composeEmail(message)
            composeWA(message)
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
            val uri = "https://api.whatsapp.com/send?phone=$Phone"+"&text=" + URLEncoder.encode(text, "UTF-8")
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