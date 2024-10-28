package com.insubria.cookingapp.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "liste_spesa")
data class ListaSpesa(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val contenuto: String,
    val dataCreazione: Long
)
