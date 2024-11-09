package com.insubria.cookingapp.recipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.insubria.cookingapp.R
import com.insubria.cookingapp.entity.Ingrediente

class IngredientAdapter(private var ingredients: List<Ingrediente>) : RecyclerView.Adapter<IngredientAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ingredientName: TextView = itemView.findViewById(R.id.textview_ingredient_name)
        val ingredientQuantity: TextView = itemView.findViewById(R.id.textview_ingredient_quantity)

        fun bind(ingredient: Ingrediente) {
            ingredientName.text = ingredient.nome
            ingredientQuantity.text = ingredient.quantita.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ingredient, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredient = ingredients[position]
        holder.ingredientName.text = ingredient.nome
        holder.ingredientQuantity.text = ingredient.quantita.toString()
    }

    override fun getItemCount() = ingredients.size

    // Funzione per aggiornare gli ingredienti con le nuove quantità
    fun updateIngredients(newIngredients: List<Ingrediente>) {
        ingredients = newIngredients
        notifyDataSetChanged()
    }
}

