package com.insubria.cookingapp.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File

class ListaSpesaService {

    fun salvaListaDellaSpesa(context: Context, contenuto: String, fileName: String = "lista_spesa.txt") {
        val file = File(context.getExternalFilesDir("shopping_lists"), fileName)
        file.writeText(contenuto)
        Log.d("ListaSpesaService", "Lista della spesa salvata in: ${file.absolutePath}")
    }

    fun condividiLista(context: Context, fileName: String = "lista_spesa.txt") {
        val file = File(context.getExternalFilesDir("shopping_lists"), fileName)
        val uri: Uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Condividi lista della spesa"))
    }
}
