package com.insubria.cookingapp.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.insubria.cookingapp.R
import com.insubria.cookingapp.View.RecipeDetailActivity
import com.insubria.cookingapp.databinding.FragmentHomeBinding
import com.insubria.cookingapp.entity.Ricetta
import com.insubria.cookingapp.recipe.RecipeAdapter
import com.insubria.cookingapp.repository.RicettaRepository
import com.insubria.cookingapp.utils.CookingAppDatabase

class HomeFragment : Fragment() {

    private lateinit var spinnerTipoPortata: Spinner
    private lateinit var spinnerTempo: Spinner
    private lateinit var spinnerDifficolta: Spinner
    private lateinit var dropdownContent: LinearLayout
    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var recyclerView: RecyclerView
    private var tipoRicettaSelezionato: String? = null
    private var tempoSelezionato: Int? = null
    private var difficoltaSelezionata: Int? = null
    private lateinit var buttonCerca: Button
    private lateinit var ricetta: RicettaRepository
    private lateinit var cercaGlobale: EditText
    private lateinit var edittextIngredienti: EditText

    private var recipes = mutableListOf<Ricetta>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cercaGlobale = view.findViewById(R.id.edittext_cerca_globale)
        edittextIngredienti = view.findViewById(R.id.edittext_ingredienti)

        ricetta = RicettaRepository(CookingAppDatabase.getDatabase(requireContext()).ricettaDao())

        recyclerView = view.findViewById(R.id.recyclerview_ricette)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recipeAdapter = RecipeAdapter(recipes) { recipe ->
            openRecipeDetail(recipe)
        }
        recyclerView.adapter = recipeAdapter

        // Osserva le ricette dal database
        ricetta.getAllRicette.observe(viewLifecycleOwner, Observer { ricette ->
            if (ricette != null) {
                recipes.clear()
                recipes.addAll(ricette)
                recipeAdapter.notifyDataSetChanged()
            }
        })

        spinnerTipoPortata = view.findViewById(R.id.spinner_categoria)
        spinnerTempo = view.findViewById(R.id.spinner_tempo)
        spinnerDifficolta = view.findViewById(R.id.spinner_difficolta)
        dropdownContent = view.findViewById(R.id.dropdown_content)

        setupSpinner(spinnerTipoPortata, R.array.categoria)
        setupSpinner(spinnerTempo, R.array.tempi)
        setupSpinner(spinnerDifficolta, R.array.difficolta)

        buttonCerca = view.findViewById(R.id.button_cerca)
        buttonCerca.setOnClickListener {
            cercaRicette()
        }

        val dropdownHeader = view.findViewById<CardView>(R.id.dropdown_header)
        dropdownHeader.setOnClickListener {
            dropdownContent.visibility = if (dropdownContent.visibility == View.GONE) View.VISIBLE else View.GONE
        }
    }

    private fun cercaRicette() {
        // Ottieni i valori dai campi di ricerca
        var testoRicerca: String? = cercaGlobale.text.toString().trim()
        if(testoRicerca.isNullOrEmpty()) testoRicerca = null
        var ingredientiTesto: String? = edittextIngredienti.text.toString().trim().takeIf { it.isNotEmpty() }
        var ingredienti: List<String>? = ingredientiTesto?.split(",")?.map { it.trim() }

        // Ottieni i valori dagli Spinner
        tipoRicettaSelezionato = spinnerTipoPortata.selectedItem?.toString().takeIf { it != "Seleziona Categoria" }
        difficoltaSelezionata = spinnerDifficolta.selectedItem?.toString()?.toIntOrNull()
        tempoSelezionato = spinnerTempo.selectedItem?.toString()?.toIntOrNull()

        // Esegui la ricerca con i filtri
        ricetta.searchRicetta(testoRicerca, tipoRicettaSelezionato, difficoltaSelezionata, tempoSelezionato, ingredienti)
            .observe(viewLifecycleOwner, Observer { ricetteFiltrate ->
                if (ricetteFiltrate.isNullOrEmpty()) {
                    // Se non ci sono ricette, mostra un messaggio di "nessun risultato"
                    Toast.makeText(requireContext(), "Nessuna ricetta trovata per i filtri selezionati", Toast.LENGTH_SHORT).show()
                } else {
                    // Se ci sono ricette, aggiorna l'adapter
                    recipes.clear()
                    recipes.addAll(ricetteFiltrate)
                    recipeAdapter.notifyDataSetChanged()
                }
            })
    }



    private fun setupSpinner(spinner: Spinner, arrayResId: Int) {
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            arrayResId,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun openRecipeDetail(recipe: Ricetta) {
        val intent = Intent(requireContext(), RecipeDetailActivity::class.java).apply {
            putExtra("recipe", recipe) // Ora puoi passare l'oggetto Ricetta
        }
        startActivity(intent)
    }

    // Aggiungi una nuova ricetta
    fun addNewRecipe(recipe: Ricetta) {
        recipes.add(recipe)
        recipeAdapter.notifyDataSetChanged()
    }
}


