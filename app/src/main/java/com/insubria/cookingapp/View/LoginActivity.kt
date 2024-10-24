package com.insubria.cookingapp.View

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.insubria.cookingapp.R
import com.insubria.cookingapp.auth.FirebaseAuthentication

class LoginActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private val auth: FirebaseAuthentication = FirebaseAuthentication()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        emailEditText = findViewById(R.id.editText1)
        passwordEditText = findViewById(R.id.editText2)
        loginButton = findViewById(R.id.loginbutton)
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim().lowercase()
            val password = passwordEditText.text.toString().trim()
            auth.signIn(email, password) { success, message ->
                if (success) {
                    //TODO: Sostituire registerActivityActivity con homeActivity
                    startActivity(Intent(this, RegisterActivity::class.java))
                } else {
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