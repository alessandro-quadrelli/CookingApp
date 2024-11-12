package com.insubria.cookingapp.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
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
    var ricettaId: Int
) : Parcelable

