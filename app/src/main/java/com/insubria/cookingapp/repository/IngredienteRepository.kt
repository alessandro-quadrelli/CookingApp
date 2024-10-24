package com.insubria.cookingapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.insubria.cookingapp.dao.IngredienteDao
import com.insubria.cookingapp.entity.Ingrediente


class IngredienteRepository(private val ingredienteDao: IngredienteDao) {

    // Inserire un ingrediente
    suspend fun insert(ingrediente: Ingrediente) {
        ingredienteDao.insert(ingrediente)
    }

    // Aggiornare un ingrediente
    suspend fun update(ingrediente: Ingrediente) {
        ingredienteDao.update(ingrediente)
    }

    // Eliminare un ingrediente
    suspend fun delete(ingrediente: Ingrediente) {
        ingredienteDao.delete(ingrediente)
    }

    fun getIngredientiByRicetta(ricettaId: Int): LiveData<List<Ingrediente>> {
        return ingredienteDao.getIngredientiByRicetta(ricettaId)
    }

    fun getIngredientiForPersone(ricettaId: Int, numeroPersone: Int): LiveData<List<Ingrediente>> {
        val liveDataIngredienti = ingredienteDao.getIngredientiByRicetta(ricettaId)
        val ingredientiForPersone = MutableLiveData<List<Ingrediente>>()

        liveDataIngredienti.observeForever { ingredientiList ->
            val updatedList = ingredientiList.map { ingrediente ->
                ingrediente.copy(quantita = ingrediente.quantita * numeroPersone)
            }
            ingredientiForPersone.postValue(updatedList)
        }

        return ingredientiForPersone
    }
}
