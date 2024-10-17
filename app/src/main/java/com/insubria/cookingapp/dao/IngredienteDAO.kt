package com.insubria.cookingapp.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.insubria.cookingapp.entity.Ingrediente

@Dao
interface IngredienteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ingrediente: Ingrediente)

    @Update
    suspend fun update(ingrediente: Ingrediente)

    @Delete
    suspend fun delete(ingrediente: Ingrediente)

    @Query("SELECT * FROM ingredienti WHERE ricettaId = :ricettaId")
    fun getIngredientiByRicetta(ricettaId: Int): LiveData<List<Ingrediente>>

    @Query("DELETE FROM ingredienti WHERE ricettaId = :ricettaId")
    suspend fun deleteIngredientiByRicetta(ricettaId: Int)
}
