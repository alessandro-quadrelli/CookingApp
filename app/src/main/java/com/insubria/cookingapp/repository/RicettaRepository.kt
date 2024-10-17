package com.insubria.cookingapp.repository

import androidx.lifecycle.LiveData
import com.insubria.cookingapp.dao.RicettaDao
import com.insubria.cookingapp.entity.Ricetta

class RicettaRepository(private val ricettaDao: RicettaDao) {

    // Ottenere tutte le ricette
    val allRicette: LiveData<List<Ricetta>> = ricettaDao.getAllRicette()

    // Inserire una ricetta
    suspend fun insert(ricetta: Ricetta) {
        ricettaDao.insert(ricetta)
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

    // Ricerca di ricette per nome
    fun searchRicetteByName(nome: String): LiveData<List<Ricetta>> {
        return ricettaDao.searchRicetteByName(nome)
    }
}
