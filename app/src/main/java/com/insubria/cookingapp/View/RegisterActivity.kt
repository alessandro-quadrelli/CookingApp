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

class RegisterActivity : AppCompatActivity() {
    private var auth: FirebaseAuthentication = FirebaseAuthentication()
    private lateinit var signUpEmail: EditText
    private lateinit var signUpPassword: EditText
    private lateinit var registerButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        signUpEmail = findViewById(R.id.signUpEmail)
        signUpPassword = findViewById(R.id.signUpPassword)
        registerButton = findViewById(R.id.registerButton)
        registerButton.setOnClickListener {
            val email = signUpEmail.text.toString().trim().lowercase()
            val password = signUpPassword.text.toString().trim()
            auth.signUp(email, password) { success, message ->
                if (success) {
                    //TODO: Sostituire LoginActivity con homeActivity
                    startActivity(Intent(this, LoginActivity::class.java))
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
    fun login(view: View) {
        startActivity(Intent(this, LoginActivity::class.java))
    }
}
