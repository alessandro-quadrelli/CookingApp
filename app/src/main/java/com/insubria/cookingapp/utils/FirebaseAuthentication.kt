package com.insubria.cookingapp.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseAuthentication {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Funzione di registrazione (sign up)
    fun signUp(email: String, password: String, callback: (Boolean, String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Registrazione avvenuta con successo
                    callback(true, "Registration successful")
                } else {
                    // Registrazione fallita
                    callback(false, task.exception?.message ?: "Registration failed")
                }
            }
    }

    // Funzione di login (sign in)
    fun signIn(email: String, password: String, callback: (Boolean, String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Login avvenuto con successo
                    callback(true, "Login successful")
                } else {
                    // Login fallito
                    callback(false, task.exception?.message ?: "Login failed")
                }
            }
    }

    // Funzione di logout
    fun signOut() {
        auth.signOut()
    }

    // Funzione per ottenere l'utente corrente
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    // Funzione per verificare se l'utente è loggato
    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }
}
