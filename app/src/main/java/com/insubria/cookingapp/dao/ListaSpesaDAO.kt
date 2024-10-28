package com.insubria.cookingapp.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.insubria.cookingapp.entity.ListaSpesa

@Dao
interface ListaSpesaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListaSpesa(listaSpesa: ListaSpesa): Long

    @Query("SELECT * FROM liste_spesa ORDER BY dataCreazione DESC")
    fun getAllListeSpesa(): LiveData<List<ListaSpesa>>

    @Update
    suspend fun updateListaSpesa(listaSpesa: ListaSpesa)

    @Delete
    suspend fun deleteListaSpesa(listaSpesa: ListaSpesa)
}
