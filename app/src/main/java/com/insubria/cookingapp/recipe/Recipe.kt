package com.insubria.cookingapp.recipe

import android.os.Parcel
import android.os.Parcelable

data class Recipe(
    val name: String,
    val prepTime: String,
    val difficulty: Int,
    val category: String,
    val imageResId: Int,
    val description: String,
    val ingredients: List<Ingredient>, // Lista di ingredienti
    val note: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.createTypedArrayList(Ingredient.CREATOR) ?: emptyList(), // Leggi la lista di ingredienti
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(prepTime)
        parcel.writeInt(difficulty)
        parcel.writeString(category)
        parcel.writeInt(imageResId)
        parcel.writeString(description)
        parcel.writeTypedList(ingredients) // Scrivi la lista di ingredienti
        parcel.writeString(note)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Recipe> {
        override fun createFromParcel(parcel: Parcel): Recipe {
            return Recipe(parcel)
        }

        override fun newArray(size: Int): Array<Recipe?> {
            return arrayOfNulls(size)
        }
    }
}




