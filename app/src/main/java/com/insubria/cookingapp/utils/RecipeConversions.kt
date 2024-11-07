package com.insubria.cookingapp.utils

import com.insubria.cookingapp.entity.Ingrediente
import com.insubria.cookingapp.recipe.Ingredient

class RecipeConversions {

    // Conversione Olio <-> Burro (1 parte di burro = 0.8 parti di olio)
    fun butterToOil(butterGrams: Double): Double {
        return butterGrams * 0.8
    }

    fun oilToButter(oilGrams: Double): Double {
        return oilGrams / 0.8
    }

    // Conversione Burro <-> Ricotta (1 parte di burro = 2 parti di ricotta)
    fun butterToRicotta(butterGrams: Double): Double {
        return butterGrams * 2.0
    }

    fun ricottaToButter(ricottaGrams: Double): Double {
        return ricottaGrams / 2.0
    }

    // Conversione Cucchiai <-> Grammi (valore medio per ingredienti comuni, come zucchero e farina)
    // 1 cucchiaio = 15 grammi (valore generico)
    fun spoonToGrams(spoons: Double): Double {
        return spoons * 15.0
    }

    fun gramsToSpoons(grams: Double): Double {
        return grams / 15.0
    }

    // Conversione Bicchieri <-> Centilitri (1 bicchiere standard = 20 cl)
    fun glassToCentiliters(glasses: Double): Double {
        return glasses * 20.0
    }

    fun centilitersToGlass(centiliters: Double): Double {
        return centiliters / 20.0
    }

    // Conversione Zucchero <-> Miele (1 parte di zucchero = 0.7 parti di miele)
    fun sugarToHoney(sugarGrams: Double): Double {
        return sugarGrams * 0.7
    }

    fun honeyToSugar(honeyGrams: Double): Double {
        return honeyGrams / 0.7
    }

    // Moltiplica la quantità per il numero di persone
    fun multiplyIngredientQuantities(ingredienti: List<Ingredient>, numeroPersone: Int): List<Ingredient> {
        return ingredienti.map { ingrediente ->
            // Moltiplica la quantità per il numero di persone
            val newQuantity = ingrediente.quantity * numeroPersone
            ingrediente.copy(quantity = newQuantity)  // Restituisce una copia dell'ingrediente con la quantità aggiornata
        }
    }
}
