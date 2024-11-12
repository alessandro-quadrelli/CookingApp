package com.insubria.cookingapp.utils

import com.insubria.cookingapp.data.dao.RicettaDao

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ImageService(private val context: Context, private val ricettaDao: RicettaDao) {

    private val storageRef = FirebaseStorage.getInstance().reference
    private val fireAuth = FirebaseAuthentication()

    // Funzione per aprire la fotocamera
    fun openCamera(activity: Activity, imageCaptureLauncher: ActivityResultLauncher<Intent>) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(context.packageManager) != null) {
            imageCaptureLauncher.launch(takePictureIntent)
        } else {
            Toast.makeText(context, "Fotocamera non disponibile", Toast.LENGTH_SHORT).show()
        }
    }

    // Gestione del risultato della fotocamera (immagine catturata)
    suspend fun handleCapturedImage(data: Intent?, ricettaId: Int): Uri? {
        val imageBitmap = data?.extras?.get("data") as Bitmap?
        return imageBitmap?.let { bitmap ->
            saveImageToFirebase(bitmap,ricettaId)
        }
    }

    // Funzione per salvare l'immagine su Firebase Storage e ottenere il riferimento URL
    private suspend fun saveImageToFirebase(bitmap: Bitmap, ricettaId: Int): Uri? {
        return withContext(Dispatchers.IO) {
            val imageName = "${fireAuth.getCurrentUser()}/images/$ricettaId.jpg"
            val imageRef = storageRef.child(imageName)

            // Converti Bitmap in array di byte
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            // Caricamento su Firebase
            val uploadTask = imageRef.putBytes(data)
            val uriTask = suspendCoroutine<Uri?> { continuation ->
                uploadTask.addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        continuation.resume(uri)
                    }.addOnFailureListener {
                        continuation.resume(null)
                    }
                }.addOnFailureListener {
                    continuation.resume(null)
                }
            }
            uriTask
        }
    }

    // Salva l'URL dell'immagine su Room associandolo alla ricetta
    suspend fun saveImageReferenceToRoom(ricettaId: Int, imageUrl: String) {
        withContext(Dispatchers.IO) {
            val recipe = ricettaDao.getRicettaById(ricettaId)
            if (recipe != null) {
                val updatedRecipe = recipe.copy(imageUrl = imageUrl)
                ricettaDao.update(updatedRecipe)
            }
        }
    }
    private suspend fun getImageFromFirebase(ricettaId: Int, callback: (Boolean, String) -> Unit){
        fireAuth.getCurrentUser()?.uid?.let {
            storageRef.child(it).child("images").child(ricettaId.toString()).downloadUrl.addOnSuccessListener { uri ->
                callback(true, uri.toString())
            }.addOnFailureListener {
                callback(false, "Immagine non trovata")
            }
        }
    }
}
