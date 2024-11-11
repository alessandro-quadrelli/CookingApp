package com.insubria.cookingapp.gallery

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.insubria.cookingapp.R
import com.insubria.cookingapp.View.MainActivity

class SLFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_shoppinglist, container, false)

        val sendText = view.findViewById<Button>(R.id.sendtext)
        val editTextShoppingList = view.findViewById<EditText>(R.id.editTextShoppingList)

        sendText.setOnClickListener {
            val shoppingListText = editTextShoppingList.text.toString() // Ottieni il testo dall'EditText

            if (shoppingListText.isNotEmpty()) {
                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, shoppingListText) // Usa il testo dall'EditText
                    type = "text/plain"
                }
                if (shareIntent.resolveActivity(requireActivity().packageManager) != null) {
                    startActivity(Intent.createChooser(shareIntent, "Condividi con"))
                } else {
                    Toast.makeText(requireContext(), "Nessuna applicazione trovata", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "La lista della spesa è vuota", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}