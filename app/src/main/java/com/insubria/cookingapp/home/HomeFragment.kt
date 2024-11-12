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
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.insubria.cookingapp.R
import com.insubria.cookingapp.View.RecipeDetailActivity
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
    private var tempoMinimo: Int? = null
    private var tempoMassimo: Int? = null
    private var difficoltaSelezionata: Int? = null
    private lateinit var buttonCerca: Button
    private lateinit var ricetta: RicettaRepository
    private lateinit var cercaGlobale: EditText

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
        // Inizializza il repository
        ricetta = RicettaRepository(CookingAppDatabase.getDatabase(requireContext()).ricettaDao())

        // Inizializza la RecyclerView
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

        // Configura gli Spinner
        spinnerTipoPortata = view.findViewById(R.id.spinner_categoria)
        spinnerTempo = view.findViewById(R.id.spinner_tempo)
        spinnerDifficolta = view.findViewById(R.id.spinner_difficolta)
        dropdownContent = view.findViewById(R.id.dropdown_content)

        setupSpinner(spinnerTipoPortata, R.array.categoria)
        setupSpinner(spinnerTempo, R.array.tempi)
        setupSpinner(spinnerDifficolta, R.array.difficolta)

        // Configura il bottone di ricerca
        buttonCerca = view.findViewById(R.id.button_cerca)
        buttonCerca.setOnClickListener {
            cercaRicette()
        }

        // Aggiungi un TextWatcher per la ricerca globale
        cercaGlobale.addTextChangedListener(object : android.text.TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) {
                cercaRicette()  // Richiama la funzione di ricerca ogni volta che il testo cambia
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Gestisci l'espansione del layout
        val dropdownHeader = view.findViewById<CardView>(R.id.dropdown_header)
        dropdownHeader.setOnClickListener {
            dropdownContent.visibility = if (dropdownContent.visibility == View.GONE) View.VISIBLE else View.GONE
        }

        // Aggiungi un listener per ogni spinner per aggiornare la ricerca quando il valore cambia
        spinnerTipoPortata.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                tipoRicettaSelezionato = if (position > 0) spinnerTipoPortata.selectedItem.toString() else null
                cercaRicette()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinnerTempo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    1 -> {
                        tempoMinimo = 0
                        tempoMassimo = 15
                    }
                    2 -> {
                        tempoMinimo = 15
                        tempoMassimo = 30
                    }
                    3 -> {
                        tempoMinimo = 30
                        tempoMassimo = 45
                    }
                    4 -> {
                        tempoMinimo = 45
                        tempoMassimo = 60
                    }
                    5 -> {
                        tempoMinimo = 60
                        tempoMassimo = 90
                    }
                    6 -> {
                        tempoMinimo = 90
                        tempoMassimo = 120
                    }
                    7 -> { // > 2 ore
                        tempoMinimo = 120
                        tempoMassimo = null
                    }
                    else -> {
                        tempoMinimo = null
                        tempoMassimo = null
                    }
                }
                cercaRicette()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinnerDifficolta.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                difficoltaSelezionata = if (position > 0) spinnerDifficolta.selectedItem.toString().toIntOrNull() else null
                cercaRicette()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun cercaRicette() {
        var testoRicerca: String? = cercaGlobale.text.toString().trim()
        if (testoRicerca.isNullOrEmpty()) testoRicerca = null

        val ricetteLiveData = ricetta.searchRicetta(
            nome = testoRicerca,
            categoria = tipoRicettaSelezionato,
            difficolta = difficoltaSelezionata,
            tempoMinimo = tempoMinimo,
            tempoMassimo = tempoMassimo
        )

        // Aggiorna la RecyclerView con i risultati della ricerca
        ricetteLiveData.observe(viewLifecycleOwner, Observer { receipes ->
            recipes.clear()
            recipes.addAll(receipes)
            recipeAdapter.notifyDataSetChanged()
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
        recipes.add(recipe) // Aggiungi la ricetta
        recipeAdapter.notifyDataSetChanged() // Notifica l'adapter
    }
}
