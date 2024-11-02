package com.insubria.cookingapp.recipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.insubria.cookingapp.R

class RecipeAdapter(
    private val recipes: List<Recipe>,
    private val onClick: (Recipe) -> Unit
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageview_recipe)
        val nameTextView: TextView = itemView.findViewById(R.id.textview_recipe_name)
        val prepTimeTextView: TextView = itemView.findViewById(R.id.textview_prep_time)
        val difficultyRatingBar: RatingBar = itemView.findViewById(R.id.ratingbar_difficulty)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recipe, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        // Popola i dati
        holder.nameTextView.text = recipe.name
        holder.prepTimeTextView.text = "${recipe.prepTime}"
        holder.difficultyRatingBar.rating = recipe.difficulty.toFloat()

        // Carica l’immagine (puoi usare librerie come Glide o Picasso per gestire le immagini)
        holder.imageView.setImageResource(recipe.imageResId)

        holder.itemView.setOnClickListener { onClick(recipe) }
    }

    override fun getItemCount() = recipes.size
}

