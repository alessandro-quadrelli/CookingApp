package com.insubria.cookingapp.View

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.insubria.cookingapp.R
import com.insubria.cookingapp.recipe.Ingredient
import com.insubria.cookingapp.recipe.IngredientAdapter
import com.insubria.cookingapp.recipe.Recipe
import com.insubria.cookingapp.utils.RecipeConversions

class RecipeDetailActivity : AppCompatActivity() {

    private lateinit var recipe: Recipe
    private lateinit var ingredientAdapter: IngredientAdapter
    private lateinit var originalIngredients: List<Ingredient>  // Variabile per tenere traccia degli ingredienti originali
    private val recipeConversion = RecipeConversions()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        // Recupera i dati dal Intent
        val recipeName = intent.getStringExtra("recipeName")
        val prepTime = intent.getStringExtra("prepTime")
        val difficulty = intent.getIntExtra("difficulty", 1)
        val category = intent.getIntExtra("categoria", 1)
        val note = intent.getIntExtra("note", 1)
        val imageResId = intent.getIntExtra("imageResId", R.drawable.dinner)
        val description = intent.getStringExtra("description")
        val ingredients = intent.getStringExtra("ingredients")
        recipe = intent.getParcelableExtra("recipe")
            ?: throw IllegalArgumentException("Recipe not found")

        // Salva gli ingredienti originali
        originalIngredients = recipe.ingredients.toList()  // Crea una copia immutabile della lista degli ingredienti originali

        // Configura la RecyclerView per gli ingredienti
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview_ingredients)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inizializza l'adapter con gli ingredienti originali
        ingredientAdapter = IngredientAdapter(recipe.ingredients)
        recyclerView.adapter = ingredientAdapter

        // Setta i dettagli della ricetta
        findViewById<TextView>(R.id.textview_recipe_name).text = recipe.name
        findViewById<TextView>(R.id.textview_category).text = recipe.category
        findViewById<TextView>(R.id.textview_difficulty).text = recipe.difficulty.toString()
        findViewById<TextView>(R.id.textview_prep_time).text = recipe.prepTime
        findViewById<TextView>(R.id.textview_description).text = recipe.description
        findViewById<ImageView>(R.id.imageview_recipe).setImageResource(recipe.imageResId)
        findViewById<RatingBar>(R.id.ratingbar_difficulty).rating = recipe.difficulty.toFloat()
        findViewById<TextView>(R.id.textview_notes).text = recipe.note

        updateCopyButtonState()

        val modifyButton = findViewById<Button>(R.id.modifyButton)
        modifyButton.setOnClickListener {
            val intent = Intent(this, UpdateRecipeActivity::class.java)
            intent.putExtra("recipe", recipe)
            startActivity(intent)
        }

        // Imposta il click listener sul bottone Calcola
        val calculateButton = findViewById<Button>(R.id.button_calculate)
        calculateButton.setOnClickListener {
            val numberOfPeople = findViewById<EditText>(R.id.edittext_people).text.toString().toIntOrNull()

            if (numberOfPeople != null && numberOfPeople > 0) {
                // Moltiplica la quantità degli ingredienti in base al numero di persone usando gli ingredienti originali
                val updatedIngredients = recipeConversion.multiplyIngredientQuantities(originalIngredients, numberOfPeople)

                // Aggiorna la lista degli ingredienti nel Recipe (non solo nell'adapter)
                recipe.ingredients = updatedIngredients

                // Aggiorna l'adapter con i nuovi ingredienti
                ingredientAdapter.updateIngredients(updatedIngredients)

                // Dopo aver aggiornato l'adapter, riabilita il bottone "Copia"
                updateCopyButtonState()
            } else {
                // Gestione del caso in cui l'utente non inserisce un numero valido
                Toast.makeText(this, "Inserisci un numero valido di persone", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Funzione per copiare gli ingredienti negli appunti
    @SuppressLint("ServiceCast")
    private fun copyIngredientsToClipboard() {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val ingredientsList = recipe.ingredients // Assicurati di prendere sempre la lista aggiornata

        // Formatta la lista di ingredienti come stringa
        val stringBuilder = StringBuilder()
        for (ingredient in ingredientsList) {
            stringBuilder.append("${ingredient.name}: ${ingredient.quantity}\n") // Aggiungi \n per separare gli ingredienti
        }

        // Verifica che ci siano ingredienti
        if (stringBuilder.isNotEmpty()) {
            val clip = ClipData.newPlainText("Ingredients", stringBuilder.toString())

            // Copia nel clipboard
            clipboard.setPrimaryClip(clip)

            // Mostra un messaggio di conferma
            Toast.makeText(this, "Ingredienti copiati!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Non ci sono ingredienti da copiare", Toast.LENGTH_SHORT).show()
        }
    }

    // Funzione per abilitare o disabilitare il bottone "Copia"
    private fun updateCopyButtonState() {
        val copyButton = findViewById<Button>(R.id.button_copy)

        // Se ci sono ingredienti, abilita il bottone, altrimenti disabilitalo
        if (recipe.ingredients.isNotEmpty()) { // Controlla direttamente la lista di ingredienti
            copyButton.isEnabled = true
            copyButton.setOnClickListener {
                copyIngredientsToClipboard() // Chiamata alla funzione di copia
            }
        } else {
            copyButton.isEnabled = false
        }
    }
}




