package com.insubria.cookingapp.recipe

import android.os.Parcel
import android.os.Parcelable

data class Ingredient(
    val name: String,
    val quantity: Float
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readFloat()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeFloat(quantity)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Ingredient> {
        override fun createFromParcel(parcel: Parcel): Ingredient {
            return Ingredient(parcel)
        }

        override fun newArray(size: Int): Array<Ingredient?> {
            return arrayOfNulls(size)
        }
    }
}