package com.insubria.cookingapp.utils

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.insubria.cookingapp.data.dao.IngredienteDao
import com.insubria.cookingapp.data.dao.RicettaDao
import com.insubria.cookingapp.data.entity.Ingrediente
import com.insubria.cookingapp.data.entity.IngredientiTypeConverter
import com.insubria.cookingapp.data.entity.Ricetta

@Database(entities = [Ricetta::class, Ingrediente::class], version = 2, exportSchema = false)
@TypeConverters(IngredientiTypeConverter::class)
abstract class CookingAppDatabase : RoomDatabase() {

    abstract fun ricettaDao(): RicettaDao
    abstract fun ingredienteDao(): IngredienteDao

    companion object {
        @Volatile
        private var INSTANCE: CookingAppDatabase? = null

        fun getDatabase(context: Context): CookingAppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CookingAppDatabase::class.java,
                    "cooking_app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}



