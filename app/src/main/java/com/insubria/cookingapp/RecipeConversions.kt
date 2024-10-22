package com.insubria.cookingapp

class RecipeConversion {

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
}
