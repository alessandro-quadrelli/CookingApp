package com.insubria.cookingapp.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ricette")
data class Ricetta(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String,
    val numeroPersone: Int,
    val tempoPreparazione: Int,
    val difficolta: Int,
    val categoria: String,
    val note: String? = null,
    val imageUrl: String? = null
)
