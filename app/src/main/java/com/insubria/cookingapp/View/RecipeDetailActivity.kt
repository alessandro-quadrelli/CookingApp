package com.insubria.cookingapp.View

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.insubria.cookingapp.R
import com.insubria.cookingapp.recipe.IngredientAdapter
import com.insubria.cookingapp.recipe.Recipe

class RecipeDetailActivity : AppCompatActivity() {

    private lateinit var recipe: Recipe

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

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

        // Configura la RecyclerView per gli ingredienti
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview_ingredients)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = IngredientAdapter(recipe.ingredients)

        findViewById<TextView>(R.id.textview_recipe_name).text = recipe.name
        findViewById<TextView>(R.id.textview_category).text = recipe.category
        findViewById<TextView>(R.id.textview_difficulty).text = recipe.difficulty.toString()
        findViewById<TextView>(R.id.textview_prep_time).text = recipe.prepTime
        findViewById<TextView>(R.id.textview_description).text = recipe.description
        findViewById<ImageView>(R.id.imageview_recipe).setImageResource(recipe.imageResId)
        findViewById<RatingBar>(R.id.ratingbar_difficulty).rating = recipe.difficulty.toFloat()
        findViewById<TextView>(R.id.textview_notes).text = recipe.note

        val modifyButton = findViewById<Button>(R.id.modifyButton)
        modifyButton.setOnClickListener {
            val intent = Intent(this, UpdateRecipeActivity::class.java)

            intent.putExtra("recipe", recipe)
            startActivity(intent)
        }
    }
}


