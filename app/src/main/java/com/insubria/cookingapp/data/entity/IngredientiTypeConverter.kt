package com.insubria.cookingapp.data.entity

import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import com.google.gson.Gson


class IngredientiTypeConverter {

    @TypeConverter
    fun fromIngredientiList(ingredienti: List<Ingrediente>?): String {
        return Gson().toJson(ingredienti)
    }

    @TypeConverter
    fun toIngredientiList(data: String): List<Ingrediente> {
        val listType = object : TypeToken<List<Ingrediente>>() {}.type
        return Gson().fromJson(data, listType)
    }
}
