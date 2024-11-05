package com.insubria.cookingapp.gallery

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.insubria.cookingapp.R

class SLFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_shoppinglist, container, false)
        val sendText = view.findViewById<Button>(R.id.sendtext)
        sendText.setOnClickListener {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Lista della spesa")
                type = "text/plain"

            }
            if(shareIntent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(Intent.createChooser(shareIntent, "Condividi con"))
            } else {
                Toast.makeText(requireContext(), "Nessuna applicazione trovata", Toast.LENGTH_SHORT).show()
            }

        }
        return view
    }
}