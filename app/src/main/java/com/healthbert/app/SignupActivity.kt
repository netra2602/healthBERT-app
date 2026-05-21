package com.healthbert.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class SignupActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Seed 3 demo users without database
        AuthManager.seedDefaultUsers(this)

        val nameEditText = findViewById<EditText>(R.id.nameEditText)
        val emailEditText = findViewById<EditText>(R.id.signupEmailEditText)
        val passwordEditText = findViewById<EditText>(R.id.signupPasswordEditText)
        val signupButton = findViewById<Button>(R.id.signupButton)
        val googleSignupButton = findViewById<Button>(R.id.googleSignupButton)
        val loginText = findViewById<TextView>(R.id.loginText)

        signupButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            val result = AuthManager.signup(this, name, email, password)

            Toast.makeText(this, result.second, Toast.LENGTH_SHORT).show()

            if (result.first) {
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        googleSignupButton.setOnClickListener {
            Toast.makeText(this, "Google signup is only UI for now", Toast.LENGTH_SHORT).show()
        }

        loginText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}