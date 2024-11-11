package com.insubria.cookingapp.View

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle

import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.insubria.cookingapp.R
import com.insubria.cookingapp.dao.RicettaDao
import com.insubria.cookingapp.entity.Ingrediente
import com.insubria.cookingapp.entity.Ricetta
import com.insubria.cookingapp.recipe.IngredientAdapter
import com.insubria.cookingapp.utils.CookingAppDatabase
import com.insubria.cookingapp.utils.RecipeConversions
import kotlinx.coroutines.launch

class RecipeDetailActivity : AppCompatActivity() {

    private val REQUEST_CODE_UPDATE_RECIPE = 1
    private lateinit var recipe: Ricetta
    private lateinit var ingredientAdapter: IngredientAdapter
    private lateinit var originalIngredients: List<Ingrediente>  // Variabile per tenere traccia degli ingredienti originali
    private val recipeConversion = RecipeConversions()
    private lateinit var ricettaDao: RicettaDao

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        // Recupera la ricetta dall'intent
        recipe = intent.getParcelableExtra("recipe")
            ?: throw IllegalArgumentException("Recipe not found")

        // Salva gli ingredienti originali
        originalIngredients = recipe.ingredienti.toList()  // Crea una copia immutabile della lista degli ingredienti originali

        // Configura la RecyclerView per gli ingredienti
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview_ingredients)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inizializza l'adapter con gli ingredienti originali
        ingredientAdapter = IngredientAdapter(recipe.ingredienti)
        recyclerView.adapter = ingredientAdapter

        // Setta i dettagli della ricetta
        setRecipeDetails(recipe)

        updateCopyButtonState()

        val modifyButton = findViewById<Button>(R.id.modifyButton)
        modifyButton.setOnClickListener {
            val intent = Intent(this, UpdateRecipeActivity::class.java)
            intent.putExtra("recipe", recipe.copy(ingredienti = ArrayList(recipe.ingredienti)))
            startActivityForResult(intent, REQUEST_CODE_UPDATE_RECIPE)
        }

        // Imposta il click listener sul bottone Calcola
        val calculateButton = findViewById<Button>(R.id.button_calculate)
        calculateButton.setOnClickListener {
            val numberOfPeople = findViewById<EditText>(R.id.edittext_people).text.toString().toIntOrNull()

            if (numberOfPeople != null && numberOfPeople > 0) {
                // Moltiplica la quantità degli ingredienti in base al numero di persone usando gli ingredienti originali
                val updatedIngredients = recipeConversion.multiplyIngredientQuantities(originalIngredients, numberOfPeople)

                // Aggiorna la lista degli ingredienti nel Recipe (non solo nell'adapter)
                recipe = recipe.copy(ingredienti = updatedIngredients)

                // Aggiorna l'adapter con i nuovi ingredienti
                ingredientAdapter.updateIngredients(updatedIngredients)

                // Dopo aver aggiornato l'adapter, riabilita il bottone "Copia"
                updateCopyButtonState()
            } else {
                // Gestione del caso in cui l'utente non inserisce un numero valido
                Toast.makeText(this, "Inserisci un numero valido di persone", Toast.LENGTH_SHORT).show()
            }
        }
        // Imposta la logica per il bottone deleteButton
        val deleteButton = findViewById<Button>(R.id.deleteButton)
        deleteButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Eliminare la ricetta?")
                .setMessage("Sei sicuro di voler eliminare questa ricetta?")
                .setPositiveButton("Sì") { dialog, _ ->
                    deleteRecipe(recipe)
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
        val database = CookingAppDatabase.getDatabase(applicationContext)
        ricettaDao = database.ricettaDao()
    }

    private fun deleteRecipe(ricetta: Ricetta) {
        lifecycleScope.launch {
            try {
                val existingRicetta = ricettaDao.getRicettaById(ricetta.id)
                if (existingRicetta == null) {
                    Toast.makeText(this@RecipeDetailActivity, "Ricetta non trovata", Toast.LENGTH_SHORT).show()
                    return@launch
                }
                ricettaDao.delete(ricetta)
                Toast.makeText(this@RecipeDetailActivity, "Ricetta eliminata", Toast.LENGTH_SHORT).show()
                finish()

            } catch (e: Exception) {
                Toast.makeText(this@RecipeDetailActivity, "Errore nell'eliminazione della ricetta", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setRecipeDetails(recipe: Ricetta) {
        findViewById<TextView>(R.id.textview_recipe_name).text = recipe.nome
        findViewById<TextView>(R.id.textview_category).text = recipe.categoria
        findViewById<TextView>(R.id.textview_difficulty).text = recipe.difficolta.toString()
        findViewById<TextView>(R.id.textview_prep_time).text = "${recipe.tempoPreparazione} min"
        findViewById<TextView>(R.id.textview_description).text = recipe.descrizione
        findViewById<TextView>(R.id.textview_notes).text = recipe.note

        findViewById<RatingBar>(R.id.ratingbar_difficulty).rating = recipe.difficolta.toFloat()

        // Usa Glide per caricare l'immagine dall'URL
        Glide.with(this)
            .load(recipe.imageUrl)
            .into(findViewById(R.id.imageview_recipe))
    }

    // Funzione per copiare gli ingredienti negli appunti
    @SuppressLint("ServiceCast")
    private fun copyIngredientsToClipboard() {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val ingredientsList = recipe.ingredienti

        // Formatta la lista di ingredienti come stringa
        val stringBuilder = StringBuilder()
        for (ingredient in ingredientsList) {
            stringBuilder.append("${ingredient.nome}: ${ingredient.quantita}\n")
        }

        // Verifica che ci siano ingredienti
        if (stringBuilder.isNotEmpty()) {
            val clip = ClipData.newPlainText("Ingredients", stringBuilder.toString())
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Ingredienti copiati!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Non ci sono ingredienti da copiare", Toast.LENGTH_SHORT).show()
        }
    }

    // Funzione per abilitare o disabilitare il bottone "Copia"
    private fun updateCopyButtonState() {
        val copyButton = findViewById<Button>(R.id.button_copy)

        // Se ci sono ingredienti, abilita il bottone, altrimenti disabilitalo
        if (recipe.ingredienti.isNotEmpty()) {
            copyButton.isEnabled = true
            copyButton.setOnClickListener {
                copyIngredientsToClipboard()
            }
        } else {
            copyButton.isEnabled = false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_UPDATE_RECIPE && resultCode == Activity.RESULT_OK) {
            data?.getParcelableExtra<Ricetta>("updatedRecipe")?.let { updatedRecipe ->
                recipe = updatedRecipe
                // Aggiorna i dettagli della ricetta con i nuovi valori
                setRecipeDetails(recipe)

                // Aggiorna gli ingredienti nella RecyclerView
                ingredientAdapter.updateIngredients(recipe.ingredienti)
                updateCopyButtonState()
            }
        }
    }
}
