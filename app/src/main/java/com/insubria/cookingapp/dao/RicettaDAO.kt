package com.insubria.cookingapp.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.insubria.cookingapp.entity.Ricetta

@Dao
interface RicettaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ricetta: Ricetta)

    @Update
    suspend fun update(ricetta: Ricetta)

    @Delete
    suspend fun delete(ricetta: Ricetta)

    @Query("SELECT * FROM ricette WHERE id = :id")
    suspend fun getRicettaById(id: Int): Ricetta?

    @Query("SELECT * FROM ricette")
    fun getAllRicette(): LiveData<List<Ricetta>>

    @Query("SELECT * FROM ricette WHERE categoria = :categoria")
    fun getRicetteByCategoria(categoria: String): LiveData<List<Ricetta>>

    @Query("SELECT * FROM ricette WHERE nome LIKE :nome")
    fun searchRicetteByName(nome: String): LiveData<List<Ricetta>>
}
