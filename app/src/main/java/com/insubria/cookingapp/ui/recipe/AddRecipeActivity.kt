package com.insubria.cookingapp.ui.recipe

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.insubria.cookingapp.R
import com.insubria.cookingapp.data.entity.Ingrediente
import com.insubria.cookingapp.data.entity.Ricetta
import com.insubria.cookingapp.data.repository.RicettaRepository
import com.insubria.cookingapp.utils.CookingAppDatabase
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import android.content.pm.PackageManager
import android.Manifest

class AddRecipeActivity : AppCompatActivity() {

    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_GALLERY_IMAGE = 2
    private val REQUEST_STORAGE_PERMISSION = 1001
    private lateinit var imageViewRecipe: ImageView
    private lateinit var linearLayoutIngredients: LinearLayout
    private lateinit var buttonAddIngredient: Button
    private lateinit var spinnerCategory: Spinner
    private lateinit var bottoneSalvaRicetta: Button
    private lateinit var ricetta : RicettaRepository
    private lateinit var editTextRecipeName: EditText
    private lateinit var editTextPreparationTime: EditText
    private lateinit var ratingBarDifficulty: RatingBar
    private lateinit var categoriaSelezionata: String
    private lateinit var editTextDescription: EditText
    private lateinit var editTextNotes: EditText
    private var imageUri: String? = null
    private val REQUEST_CAMERA_PERMISSION = 1002

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
        editTextPreparationTime = findViewById(R.id.editTextPreparationTime)
        ratingBarDifficulty = findViewById(R.id.ratingBarDifficulty)

        buttonAddIngredient.setOnClickListener {
            addIngredient()
        }
        bottoneSalvaRicetta = findViewById(R.id.buttonSaveRecipe)
        editTextRecipeName = findViewById(R.id.editTextRecipeName)
        editTextDescription = findViewById(R.id.editTextDescription)
        editTextNotes = findViewById(R.id.editTextNotes)

        spinnerCategory.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                categoriaSelezionata = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                categoriaSelezionata = null.toString()
            }
        }
        ricetta = RicettaRepository(CookingAppDatabase.getDatabase(this).ricettaDao())
        bottoneSalvaRicetta.setOnClickListener {
            val id = 0
            val name = editTextRecipeName.text.toString()
            val preparationTime = editTextPreparationTime.text.toString().trim().toIntOrNull() ?: 0
            val difficulty = ratingBarDifficulty.rating.toInt()
            val category = spinnerCategory.selectedItem.toString().trim()
            val ingredientiViews = linearLayoutIngredients.children
            val ingredienti = ingredientiViews.map { view ->
                val ingredientName = view.findViewById<EditText>(R.id.textview_ingredient_name).text.toString()
                val ingredientQuantity = view.findViewById<EditText>(R.id.edittextview_ingredient_quantity).text.toString().toFloatOrNull() ?: 0f
                Ingrediente(nome = ingredientName, quantita = ingredientQuantity, ricettaId = 0)
            }.toList()
            val descrizione = editTextDescription.text.toString()
            val note = editTextNotes.text.toString()
            val foto = imageUri

            val recipe =  Ricetta(id, name, preparationTime, difficulty, category, ingredienti, descrizione, note, foto)
            lifecycleScope.launch {
                ricetta.insert(recipe)
                Toast.makeText(this@AddRecipeActivity, "Ricetta aggiunta con successo!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun setupSpinner() {
        val categories = resources.getStringArray(R.array.categoria)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = adapter
    }

    private fun addIngredient() {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val ingredientView = inflater.inflate(R.layout.item_addingredient, null)
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        } else {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private fun openGallery() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_STORAGE_PERMISSION)
        } else {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, REQUEST_GALLERY_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    imageViewRecipe.setImageBitmap(imageBitmap)
                    val uri = saveImageToInternalStorage(imageBitmap)
                    imageUri = uri.toString()
                }
                REQUEST_GALLERY_IMAGE -> {
                    val selectedImageUri = data?.data
                    imageUri = selectedImageUri?.toString()
                    imageViewRecipe.setImageURI(selectedImageUri)
                }
            }
        }
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): Uri {
        val filename = "recipe_${System.currentTimeMillis()}.png"
        val file = File(filesDir, filename)
        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return Uri.fromFile(file)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_STORAGE_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery()
                } else {
                    Toast.makeText(this, "Permesso di lettura memoria esterna negato. Non potrai caricare immagini dalla galleria.", Toast.LENGTH_SHORT).show()
                }
            }
            REQUEST_CAMERA_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else {
                    Toast.makeText(this, "Permesso di utilizzo della fotocamera negato. Non potrai scattare foto.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupLayoutSpinner() {
        val categories = resources.getStringArray(R.array.categoria)
        val adapter = ArrayAdapter(this, R.layout.spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = adapter
    }
}
