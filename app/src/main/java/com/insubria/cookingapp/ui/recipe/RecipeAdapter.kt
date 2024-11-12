package com.insubria.cookingapp.ui.recipe

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.insubria.cookingapp.R
import com.insubria.cookingapp.data.entity.Ricetta

class RecipeAdapter(
    private var recipes: List<Ricetta>,
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

        // Popola i dati di base
        holder.nameTextView.text = "Titolo:           ${recipe.nome}"
        holder.prepTimeTextView.text = "${recipe.tempoPreparazione} min"
        holder.difficultyRatingBar.rating = recipe.difficolta.toFloat()

        // Gestisci il caricamento dell'immagine
        if (!recipe.imageUrl.isNullOrEmpty()) {
            val imagePath = Uri.parse(recipe.imageUrl)
            Glide.with(holder.itemView.context)
                .load(imagePath)
                .placeholder(R.drawable.default_image) // Immagine di placeholder mentre si carica
                .error(R.drawable.default_image) // Immagine di default in caso di errore
                .into(holder.imageView)
        } else {
            // Se l'URL dell'immagine è nullo o vuoto, usa l'immagine di default
            holder.imageView.setImageResource(R.drawable.default_image)
        }

        // Gestisci il clic sull'elemento della lista
        holder.itemView.setOnClickListener { onClick(recipe) }
    }


    fun updateData(nuoveRicette: List<Ricetta>) {
        this.recipes = nuoveRicette
        notifyDataSetChanged()
    }
    override fun getItemCount() = recipes.size
}

