package com.insubria.cookingapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.insubria.cookingapp.R
import com.insubria.cookingapp.ui.home.MainActivity
import com.insubria.cookingapp.utils.FirebaseAuthentication

class LoginActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private val auth: FirebaseAuthentication = FirebaseAuthentication()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        emailEditText = findViewById(R.id.signUpEmail)
        passwordEditText = findViewById(R.id.signUpPassword)
        loginButton = findViewById(R.id.loginbutton)
        loginButton.setOnClickListener {
            Log.d("LoginActivity", "Login button clicked")
            val email = emailEditText.text.toString().trim().lowercase()
            val password = passwordEditText.text.toString().trim()
            auth.signIn(email, password) { success, message ->
                if (success) {
                    Log.d("LoginActivity", "Login successful")
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    Log.d("LoginActivity", "Login failed: $message")
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun register(view: View) {
        startActivity(Intent(this, RegisterActivity::class.java))
    }
}