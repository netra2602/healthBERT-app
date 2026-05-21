package com.healthbert.app

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class ProfileActivity : Activity() {

    private val profilePrefs = "HealthBERTProfile"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val backToDashboardButton = findViewById<TextView>(R.id.backToDashboardButton)

        val nameEditText = findViewById<EditText>(R.id.nameEditText)
        val ageEditText = findViewById<EditText>(R.id.ageEditText)
        val genderEditText = findViewById<EditText>(R.id.genderEditText)
        val medicalConditionsEditText = findViewById<EditText>(R.id.medicalConditionsEditText)
        val allergiesEditText = findViewById<EditText>(R.id.allergiesEditText)
        val emergencyContactEditText = findViewById<EditText>(R.id.emergencyContactEditText)
        val saveProfileButton = findViewById<Button>(R.id.saveProfileButton)

        val prefs = getSharedPreferences(profilePrefs, Context.MODE_PRIVATE)

        nameEditText.setText(prefs.getString("name", ""))
        ageEditText.setText(prefs.getString("age", ""))
        genderEditText.setText(prefs.getString("gender", ""))
        medicalConditionsEditText.setText(prefs.getString("medicalConditions", ""))
        allergiesEditText.setText(prefs.getString("allergies", ""))
        emergencyContactEditText.setText(prefs.getString("emergencyContact", ""))

        backToDashboardButton.setOnClickListener {
            hideKeyboard()
            finish()
        }

        saveProfileButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val age = ageEditText.text.toString().trim()
            val gender = genderEditText.text.toString().trim()
            val medicalConditions = medicalConditionsEditText.text.toString().trim()
            val allergies = allergiesEditText.text.toString().trim()
            val emergencyContact = emergencyContactEditText.text.toString().trim()

            hideKeyboard()

            if (name.isEmpty() || age.isEmpty() || gender.isEmpty()) {
                Toast.makeText(this, "Please enter name, age, and gender", Toast.LENGTH_SHORT).show()
            } else {
                prefs.edit()
                    .putString("name", name)
                    .putString("age", age)
                    .putString("gender", gender)
                    .putString("medicalConditions", medicalConditions)
                    .putString("allergies", allergies)
                    .putString("emergencyContact", emergencyContact)
                    .apply()

                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun hideKeyboard() {
        val view = currentFocus
        if (view != null) {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            view.clearFocus()
        }
    }
}