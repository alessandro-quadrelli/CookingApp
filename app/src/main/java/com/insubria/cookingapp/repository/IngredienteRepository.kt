package com.insubria.cookingapp.repository

import androidx.lifecycle.LiveData
import com.insubria.cookingapp.dao.IngredienteDao
import com.insubria.cookingapp.entity.Ingrediente


class IngredienteRepository(private val ingredienteDao: IngredienteDao) {

    // Inserire un ingrediente
    suspend fun insert(ingrediente: Ingrediente) {
        ingredienteDao.insert(ingrediente)
    }

    // Inserire una lista di ingredienti
    suspend fun insertAll(ingredienti: List<Ingrediente>) {
        ingredienteDao.insertAll(ingredienti)
    }

    // Aggiornare un ingrediente
    suspend fun update(ingrediente: Ingrediente) {
        ingredienteDao.update(ingrediente)
    }

    // Eliminare un ingrediente
    suspend fun delete(ingrediente: Ingrediente) {
        ingredienteDao.delete(ingrediente)
    }

    // Eliminare tutti gli ingredienti associati a una ricetta specifica
    suspend fun deleteIngredientiByRicetta(ricettaId: Int) {
        ingredienteDao.deleteIngredientiByRicetta(ricettaId)
    }

    // Ottenere tutti gli ingredienti associati a una ricetta specifica
    fun getIngredientiByRicetta(ricettaId: Int): LiveData<List<Ingrediente>> {
        return ingredienteDao.getIngredientiByRicetta(ricettaId)
    }

}
