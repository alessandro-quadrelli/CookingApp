package com.insubria.cookingapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.insubria.cookingapp.dao.IngredienteDao
import com.insubria.cookingapp.dao.RicettaDao
import com.insubria.cookingapp.entity.Ingrediente
import com.insubria.cookingapp.entity.Ricetta

@Database(entities = [Ricetta::class, Ingrediente::class], version = 1, exportSchema = false)
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
