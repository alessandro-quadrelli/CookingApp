package com.insubria.cookingapp.recipe

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.insubria.cookingapp.R
import com.insubria.cookingapp.entity.Ricetta
import java.io.File

class RecipeAdapter(
    private val recipes: List<Ricetta>,
    private val onClick: (Ricetta) -> Unit
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
        holder.nameTextView.text = recipe.nome
        holder.prepTimeTextView.text = recipe.tempoPreparazione.toString()
        holder.difficultyRatingBar.rating = recipe.difficolta.toFloat()

        // Carica l’immagine dalla posizione locale usando Glide
        val imagePath = recipe.imageUrl
            val imageUri = Uri.parse(imagePath)
            Glide.with(holder.itemView.context)
                .load(imageUri)
                .into(holder.imageView)

        holder.itemView.setOnClickListener { onClick(recipe) }
    }

    override fun getItemCount() = recipes.size
}

