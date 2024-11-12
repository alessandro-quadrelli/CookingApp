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
    suspend fun insert(ricetta: Ricetta): Long

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

    @Query("""
    SELECT DISTINCT r.* FROM ricette r
    LEFT JOIN ingredienti i ON r.id = i.ricettaId
    WHERE (:nome IS NULL OR r.nome LIKE '%' || :nome || '%')
    AND (:categoria IS NULL OR r.categoria = :categoria)
    AND (:difficolta IS NULL OR r.difficolta = :difficolta)
    AND (:tempoMinimo IS NULL OR r.tempoPreparazione >= :tempoMinimo)
    AND (:tempoMassimo IS NULL OR r.tempoPreparazione <= :tempoMassimo)
""")
    fun searchRecipes(
        nome: String? = null,
        categoria: String? = null,
        difficolta: Int? = null,
        tempoMinimo: Int? = null,
        tempoMassimo: Int? = null
    ): LiveData<List<Ricetta>>

}
