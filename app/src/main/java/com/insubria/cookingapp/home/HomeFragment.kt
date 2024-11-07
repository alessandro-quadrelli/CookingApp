package com.insubria.cookingapp.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.insubria.cookingapp.R
import com.insubria.cookingapp.View.MainActivity
import com.insubria.cookingapp.View.RecipeDetailActivity
import com.insubria.cookingapp.databinding.FragmentHomeBinding
import com.insubria.cookingapp.recipe.Ingredient
import com.insubria.cookingapp.recipe.Recipe
import com.insubria.cookingapp.recipe.RecipeAdapter

class HomeFragment : Fragment() {

    //private lateinit var spinnerTipologia: Spinner
    private lateinit var spinnerTipoPortata: Spinner
    private lateinit var spinnerTempo: Spinner
    private lateinit var spinnerDifficolta: Spinner
    private lateinit var dropdownContent: LinearLayout
    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var recyclerView: RecyclerView
    private val recipes = mutableListOf(
        Recipe(
            name = "Spaghetti alla Carbonara",
            prepTime = "20 min",
            difficulty = 3,
            category = "Primo",
            imageResId = R.drawable.dinner, // Usa l'ID immagine appropriato
            description = "Ridurre a bastoncini il guanciale. Da sapere: come dice il nome, il guanciale è ricavato dalla guancia del maiale, mentre la pancetta dal ventre. Per il bacon, invece, si utilizzano diverse parti del maiale.\n" +
                    "Raccogliere i tuorli in una ciotola. Peparli, unire un mix di pecorino e grana grattugiati, un pizzico di sale e amalgamare con la frusta. Stemperare il composto con g 40-50 di acqua fredda." +
                    "Mettere a cuocere gli spaghetti in acqua bollente salata. Poi rosolare a fuoco basso il guanciale in padella con 3 cucchiai di olio extravergine di oliva, facendo in modo che sia pronto insieme alla pasta (se utilizzate la pancetta calcolare 7-8 minuti)." +
                    "Scolare al dente gli spaghetti senza sgocciolarli troppo (un po' di acqua di cottura è utile per rendere più cremoso il condimento) e rimetterli nella casseruola calda. Condirli con il guanciale appena rosolato e il suo grasso. Mescolare bene." +
                    "Subito dopo versare il composto di tuorli sulla pasta, mescolare velocemente e servire all'istante. Completate con pecorino grattugiato e una macinata di pepe.\n",
            ingredients = mutableListOf(
                Ingredient("Spaghetti", 200f), // Quantità in Float
                Ingredient("Uova", 2f),
                Ingredient("Pancetta", 150f),
                Ingredient("Pecorino", 50f),
                Ingredient("Pepe nero", 0.5f) // Quantità in Float, puoi usare 0.5 per "q.b."
            ),
            note ="Importante: non bisogna cuocere le uova, unire la salsa a fuoco spento."
        ),
        Recipe(
            name = "Risotto alla Milanese",
            prepTime = "30 min",
            difficulty = 2,
            category = "Primo",
            imageResId = R.drawable.pizza3,
            description = "Un risotto cremoso e saporito, tipico della cucina milanese.",
            ingredients = mutableListOf(
                Ingredient("Riso", 300f),
                Ingredient("Brodo", 1f), // Assumendo 1L di brodo come 1
                Ingredient("Zafferano", 0.1f), // Quantità in grammi o bustine
                Ingredient("Cipolla", 1f),
                Ingredient("Burro", 50f),
                Ingredient("Parmigiano", 50f)
            ),
            note = "Usa sempre brodo caldo per una cottura uniforme."
        ),
        // Aggiungi altre ricette qui: da implementare l'aggiunta dinamica
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inizializza gli Spinner con view.findViewById
        spinnerTipoPortata = view.findViewById(R.id.spinner_categoria)
        spinnerTempo = view.findViewById(R.id.spinner_tempo)
        spinnerDifficolta = view.findViewById(R.id.spinner_difficolta)
        dropdownContent = view.findViewById(R.id.dropdown_content)

        // Set Adapter per ogni Spinner
        setupSpinner(spinnerTipoPortata, R.array.categoria)
        setupSpinner(spinnerTempo, R.array.tempi)
        setupSpinner(spinnerDifficolta, R.array.difficolta)

        // Gestisci espansione del layout
        val dropdownHeader = view.findViewById<CardView>(R.id.dropdown_header)
        dropdownHeader.setOnClickListener {
            // Controlla visibilità e alterna tra VISIBLE e GONE
            if (dropdownContent.visibility == View.GONE) {
                dropdownContent.visibility = View.VISIBLE
            } else {
                dropdownContent.visibility = View.GONE
            }
        }

        val portataAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.categoria,
            android.R.layout.simple_spinner_item
        )
        portataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipoPortata.adapter = portataAdapter

        val tempoAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.tempi,
            android.R.layout.simple_spinner_item
        )
        tempoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTempo.adapter = tempoAdapter

        val difficoltaAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.difficolta,
            android.R.layout.simple_spinner_item
        )
        difficoltaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDifficolta.adapter = difficoltaAdapter

        recyclerView = view.findViewById(R.id.recyclerview_ricette)

        recipeAdapter = RecipeAdapter(recipes) { recipe ->
            // Gestione del clic su un elemento: apri RecipeDetailActivity
            openRecipeDetail(recipe)
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = recipeAdapter
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

    private fun openRecipeDetail(recipe: Recipe) {
        val intent = Intent(requireContext(), RecipeDetailActivity::class.java).apply {
            putExtra("recipe", recipe) // Ora può passare l'oggetto Recipe
        }
        startActivity(intent)
    }
    // Aggiungi una nuova ricetta
    fun addNewRecipe(recipe: Recipe) {
        recipes.add(recipe) // Aggiungi la ricetta
        recipeAdapter.notifyDataSetChanged() // Notifica l'adapter
    }
}