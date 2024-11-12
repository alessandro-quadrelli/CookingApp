package com.insubria.cookingapp.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "ricette")
@TypeConverters(IngredientiTypeConverter::class)
data class Ricetta(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String,
    val tempoPreparazione: Int,
    val difficolta: Int,
    val categoria: String?,
    var ingredienti: List<Ingrediente>,
    val descrizione: String?,
    val note: String? = null,
    val imageUrl: String? = null
) : Parcelable
