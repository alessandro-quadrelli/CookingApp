package com.insubria.cookingapp.repository

import androidx.lifecycle.LiveData
import com.insubria.cookingapp.dao.ListaSpesaDao
import com.insubria.cookingapp.entity.ListaSpesa

class ListaSpesaRepository(private val listaSpesaDao: ListaSpesaDao) {

    suspend fun createListaSpesa(testo: String): Long {
        val listaSpesa = ListaSpesa(contenuto = testo, dataCreazione = System.currentTimeMillis())
        return listaSpesaDao.insertListaSpesa(listaSpesa)
    }

    fun getAllListeSpesa(): LiveData<List<ListaSpesa>> {
        return listaSpesaDao.getAllListeSpesa()
    }

    suspend fun updateListaSpesa(listaSpesa: ListaSpesa) {
        listaSpesaDao.updateListaSpesa(listaSpesa)
    }

    suspend fun deleteListaSpesa(listaSpesa: ListaSpesa) {
        listaSpesaDao.deleteListaSpesa(listaSpesa)
    }
}
