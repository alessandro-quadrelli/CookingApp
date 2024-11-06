package com.insubria.cookingapp.View

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.insubria.cookingapp.R

class UpdateRecipeActivity : AppCompatActivity() {
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_GALLERY_IMAGE = 2
    private lateinit var imageViewRecipe: ImageView
    private lateinit var linearLayoutIngredients: LinearLayout
    private lateinit var buttonAddIngredient: Button
    private lateinit var spinnerCategory: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        spinnerCategory = findViewById(R.id.spinnerCategory)
        setupSpinner()
        setupLayoutSpinner()

        imageViewRecipe = findViewById(R.id.imageViewRecipe)
        val buttonUploadImage: Button = findViewById(R.id.buttonUploadImage)

        buttonUploadImage.setOnClickListener {
            showImageSourceDialog()
        }

        linearLayoutIngredients = findViewById(R.id.linearLayoutIngredients)
        buttonAddIngredient = findViewById(R.id.buttonAddIngredient)

        buttonAddIngredient.setOnClickListener {
            addIngredient()
        }
    }

    private fun setupSpinner() {
        // Carica l'array di stringhe dal file strings.xml
        val categories = resources.getStringArray(R.array.categoria)

        // Crea un ArrayAdapter usando l'array di stringhe e un layout di default per il spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Imposta l'adapter sullo spinner
        spinnerCategory.adapter = adapter

        // Aggiungi un listener per gestire le selezioni (opzionale)
        spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                //val selectedCategory = categories[position]
                //Toast.makeText(this@AddRecipeActivity, "Selezionato: $selectedCategory", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Gestisci il caso in cui non viene selezionato nulla, obbligatorio mettere la categoria per i filtri
            }
        }
    }

    private fun addIngredient() {
        // Inflate the ingredient item layout
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val ingredientView = inflater.inflate(R.layout.item_addingredient, null)

        // Add the inflated layout to the LinearLayout
        linearLayoutIngredients.addView(ingredientView)
    }

    private fun showImageSourceDialog() {
        val options = arrayOf("Camera", "Gallery")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Seleziona l'origine dell'immagine")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> openCamera() // Fotocamera
                1 -> openGallery() // Galleria
            }
        }
        builder.show()
    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, REQUEST_GALLERY_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    imageViewRecipe.setImageBitmap(imageBitmap)
                }
                REQUEST_GALLERY_IMAGE -> {
                    val selectedImageUri = data?.data
                    imageViewRecipe.setImageURI(selectedImageUri)
                }
            }
        }
    }

    private fun setupLayoutSpinner() {
        val categories = resources.getStringArray(R.array.categoria)

        // Crea un ArrayAdapter usando il layout personalizzato
        val adapter = ArrayAdapter(this, R.layout.spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerCategory.adapter = adapter

        spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                //val selectedCategory = categories[position]
                //Toast.makeText(this@AddRecipeActivity, "Selezionato: $selectedCategory", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Gestisci il caso in cui non viene selezionato nulla
            }
        }
    }
}