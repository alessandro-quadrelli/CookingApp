package com.insubria.cookingapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.insubria.cookingapp.dao.IngredienteDao
import com.insubria.cookingapp.entity.Ingrediente
import com.insubria.cookingapp.repository.IngredienteRepository
import com.insubria.cookingapp.ui.theme.CookingAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var ingrediente = Ingrediente(0, "Olio", 2.3f, "2", 0)
        var ingredienteRepo = IngredienteRepository(CookingAppDatabase.getDatabase(this).ingredienteDao())
        lifecycleScope.launch {
            ingredienteRepo.insert(ingrediente)
            ingredienteRepo.getIngredientiByRicetta(0).observe(this@MainActivity, Observer { ingredienti ->
                ingredienti?.let {
                    it.forEach { ingrediente ->
                        Log.d("TAG",ingrediente.toString())
                    }
                }
            })
        }
        setContent {
            CookingAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CookingAppTheme {
        Greeting("Android")
    }
}