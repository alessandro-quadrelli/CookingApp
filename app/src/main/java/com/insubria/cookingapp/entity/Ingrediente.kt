package com.insubria.cookingapp.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "ingredienti",
    foreignKeys = [ForeignKey(
        entity = Ricetta::class,
        parentColumns = ["id"],
        childColumns = ["ricettaId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["ricettaId"])]
)
data class Ingrediente(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String,
    val quantita: Float,
    val unita: String,
    val ricettaId: Int
)

