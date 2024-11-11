package com.insubria.cookingapp.View

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.insubria.cookingapp.R
import com.insubria.cookingapp.entity.Ingrediente
import com.insubria.cookingapp.entity.Ricetta
import com.insubria.cookingapp.repository.RicettaRepository
import com.insubria.cookingapp.utils.CookingAppDatabase
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class UpdateRecipeActivity : AppCompatActivity() {

    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_GALLERY_IMAGE = 2
    private val REQUEST_STORAGE_PERMISSION = 1001
    private val REQUEST_CAMERA_PERMISSION = 1002
    private lateinit var imageViewRecipe: ImageView
    private lateinit var linearLayoutIngredients: LinearLayout
    private lateinit var buttonAddIngredient: Button
    private lateinit var spinnerCategory: Spinner
    private lateinit var editTextRecipeName: EditText
    private lateinit var editTextPreparationTime: EditText
    private lateinit var ratingBarDifficulty: RatingBar
    private lateinit var editTextDescription: EditText
    private lateinit var editTextNotes: EditText
    private lateinit var buttonUpdateRecipe: Button
    private lateinit var ricettaRepository: RicettaRepository
    private var recipeId: Int = 0
    private var imageUri: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_recipe)

        ricettaRepository = RicettaRepository(CookingAppDatabase.getDatabase(this).ricettaDao())

        // Ottieni gli elementi UI
        spinnerCategory = findViewById(R.id.spinnerCategoryUpdate)
        setupSpinner()

        imageViewRecipe = findViewById(R.id.imageViewRecipeUpdate)
        editTextRecipeName = findViewById(R.id.editTextRecipeNameUpdate)
        editTextPreparationTime = findViewById(R.id.editTextPreparationTimeUpdate)
        ratingBarDifficulty = findViewById(R.id.ratingBarDifficultyUpdate)
        editTextDescription = findViewById(R.id.editTextDescriptionUpdate)
        editTextNotes = findViewById(R.id.editTextNotesUpdate)
        linearLayoutIngredients = findViewById(R.id.linearLayoutIngredientsUpdate)
        buttonAddIngredient = findViewById(R.id.buttonAddIngredientUpdate)
        buttonUpdateRecipe = findViewById(R.id.buttonUpdateRecipe)

        // Carica la ricetta esistente dai dati dell'intento
        val recipe = intent.getParcelableExtra<Ricetta>("recipe")
        recipe?.let {
            recipeId = it.id
            loadRecipe(it)
        }

        // Setup per il pulsante di caricamento immagine
        val buttonUploadImage: Button = findViewById(R.id.buttonUploadImageUpdate)
        buttonUploadImage.setOnClickListener {
            showImageSourceDialog()
        }

        // Setup per il pulsante di aggiunta degli ingredienti
        buttonAddIngredient.setOnClickListener {
            addIngredient()
        }

        // Setup per il pulsante di salvataggio delle modifiche
        buttonUpdateRecipe.setOnClickListener {
            UpdatedRecipe()
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

    private fun loadRecipe(recipe: Ricetta) {
        editTextRecipeName.setText(recipe.nome)
        editTextPreparationTime.setText(recipe.tempoPreparazione.toString())
        ratingBarDifficulty.rating = recipe.difficolta.toFloat()
        spinnerCategory.setSelection(resources.getStringArray(R.array.categoria).indexOf(recipe.categoria))
        editTextDescription.setText(recipe.descrizione)
        editTextNotes.setText(recipe.note)

        // Carica l'immagine della ricetta o un'immagine di default
        imageUri = recipe.imageUrl
        if (imageUri.isNullOrEmpty()) {
            imageViewRecipe.setImageResource(R.drawable.default_image)
        } else {
            Glide.with(this).load(imageUri).into(imageViewRecipe)
        }

        // Carica gli ingredienti esistenti nella LinearLayout
        recipe.ingredienti.forEach { ingrediente ->
            val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val ingredientView = inflater.inflate(R.layout.item_addingredient, null)
            val ingredientName = ingredientView.findViewById<EditText>(R.id.textview_ingredient_name)
            val ingredientQuantity = ingredientView.findViewById<EditText>(R.id.edittextview_ingredient_quantity)
            ingredientName.setText(ingrediente.nome)
            ingredientQuantity.setText(ingrediente.quantita.toString())
            linearLayoutIngredients.addView(ingredientView)
        }
    }

    private fun UpdatedRecipe() {
        val name = editTextRecipeName.text.toString()
        val preparationTime = editTextPreparationTime.text.toString().trim().toIntOrNull() ?: 0
        val difficulty = ratingBarDifficulty.rating.toInt()
        val category = spinnerCategory.selectedItem.toString().trim()
        val ingredientiViews = linearLayoutIngredients.children
        val ingredienti = ingredientiViews.map { view ->
            val ingredientName = view.findViewById<EditText>(R.id.textview_ingredient_name).text.toString()
            val ingredientQuantity = view.findViewById<EditText>(R.id.edittextview_ingredient_quantity).text.toString().toFloatOrNull() ?: 0f
            Ingrediente(nome = ingredientName, quantita = ingredientQuantity, ricettaId = recipeId)
        }.toList()
        val descrizione = editTextDescription.text.toString()
        val note = editTextNotes.text.toString()
        val foto = imageUri

        val updatedRecipe = Ricetta(recipeId, name, preparationTime, difficulty, category, ingredienti, descrizione, note, foto)

        lifecycleScope.launch {
            ricettaRepository.update(updatedRecipe)
            val returnIntent = Intent()
            returnIntent.putExtra("updatedRecipe", updatedRecipe)
            setResult(Activity.RESULT_OK, returnIntent)
            Toast.makeText(this@UpdateRecipeActivity, "Ricetta aggiornata con successo!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun showImageSourceDialog() {
        val options = arrayOf("Camera", "Gallery")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Seleziona l'origine dell'immagine")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> openCamera()
                1 -> openGallery()
            }
        }
        builder.show()
    }

    private fun openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        } else {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private fun openGallery() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
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
}
