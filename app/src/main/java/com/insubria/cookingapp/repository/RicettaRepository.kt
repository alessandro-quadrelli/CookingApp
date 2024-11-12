package com.insubria.cookingapp.repository

import androidx.lifecycle.LiveData
import com.insubria.cookingapp.dao.RicettaDao
import com.insubria.cookingapp.entity.Ricetta

class RicettaRepository(private val ricettaDao: RicettaDao) {


    // Ottenere tutte le ricette
    val getAllRicette: LiveData<List<Ricetta>> = ricettaDao.getAllRicette()

    // Inserire una ricetta
    suspend fun insert(ricetta: Ricetta): Long {
        return ricettaDao.insert(ricetta)
    }

    // Aggiornare una ricetta
    suspend fun update(ricetta: Ricetta) {
        ricettaDao.update(ricetta)
    }

    // Eliminare una ricetta
    suspend fun delete(ricetta: Ricetta) {
        ricettaDao.delete(ricetta)
    }

    // Ottenere una ricetta per ID
    suspend fun getRicettaById(id: Int): Ricetta? {
        return ricettaDao.getRicettaById(id)
    }

    fun searchRicetta(
        nome: String? = null,
        categoria: String? = null,
        difficolta: Int? = null,
        tempoMinimo: Int? = null,
        tempoMassimo: Int? = null,
        ingredienti: List<String>? = null
    ): LiveData<List<Ricetta>> {
        return ricettaDao.searchRecipes(nome, categoria, difficolta, tempoMinimo, tempoMassimo)
    }

}
