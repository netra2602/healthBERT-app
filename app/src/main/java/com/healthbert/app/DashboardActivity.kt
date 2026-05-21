package com.healthbert.app

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.Toast

class DashboardActivity : Activity() {

    private val symptomsList = listOf(
        "Fever",
        "Fatigue",
        "Fainting",
        "Frequent urination",
        "Feeling cold",
        "Feeling dizzy",
        "Headache",
        "Heartburn",
        "High blood pressure",
        "Hair loss",
        "Cough",
        "Cold",
        "Chest pain",
        "Constipation",
        "Confusion",
        "Nausea",
        "Neck pain",
        "Nose bleeding",
        "Vomiting",
        "Vision problem",
        "Back pain",
        "Body pain",
        "Breathing difficulty",
        "Burning sensation",
        "Sore throat",
        "Shortness of breath",
        "Stomach pain",
        "Skin rash",
        "Sweating",
        "Diarrhea",
        "Dizziness",
        "Dry mouth",
        "Ear pain",
        "Eye redness",
        "Joint pain",
        "Loss of appetite",
        "Muscle weakness",
        "Runny nose",
        "Weight loss",
        "Weight gain"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Apply saved light/dark theme when dashboard opens
        ThemeManager.applyTheme(this)

        val profileMenuButton = findViewById<LinearLayout>(R.id.profileMenuButton)
        val analyzeReviewButton = findViewById<Button>(R.id.analyzeReviewButton)
        val symptomSearchEditText = findViewById<AutoCompleteTextView>(R.id.symptomSearchEditText)
        val searchButton = findViewById<Button>(R.id.searchButton)
        val uploadReportButton = findViewById<Button>(R.id.uploadReportButton)
        val healthHistoryButton = findViewById<Button>(R.id.healthHistoryButton)
        val recentActivityButton = findViewById<Button>(R.id.recentActivityButton)

        profileMenuButton.setOnClickListener {
            hideKeyboard()

            val popupMenu = PopupMenu(this, profileMenuButton)

            popupMenu.menu.add("Profile")
            popupMenu.menu.add("Settings")
            popupMenu.menu.add("About Us")

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.title.toString()) {
                    "Profile" -> {
                        val intent = Intent(this, ProfileActivity::class.java)
                        startActivity(intent)
                        true
                    }

                    "Settings" -> {
                        val intent = Intent(this, SettingsActivity::class.java)
                        startActivity(intent)
                        true
                    }

                    "About Us" -> {
                        val intent = Intent(this, AboutUsActivity::class.java)
                        startActivity(intent)
                        true
                    }

                    else -> false
                }
            }

            popupMenu.show()
        }

        analyzeReviewButton.setOnClickListener {
            hideKeyboard()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val defaultAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            symptomsList
        )

        symptomSearchEditText.setAdapter(defaultAdapter)
        symptomSearchEditText.threshold = 1

        symptomSearchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                val typedText = s.toString().trim().lowercase()

                if (typedText.isNotEmpty()) {
                    val filteredSymptoms = symptomsList.filter {
                        it.lowercase().startsWith(typedText)
                    }

                    val filteredAdapter = ArrayAdapter(
                        this@DashboardActivity,
                        android.R.layout.simple_dropdown_item_1line,
                        filteredSymptoms
                    )

                    symptomSearchEditText.setAdapter(filteredAdapter)

                    if (filteredSymptoms.isNotEmpty()) {
                        symptomSearchEditText.showDropDown()
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        symptomSearchEditText.setOnItemClickListener { parent, _, position, _ ->
            val selectedSymptom = parent.getItemAtPosition(position).toString()

            symptomSearchEditText.setText(selectedSymptom, false)
            symptomSearchEditText.clearFocus()
            symptomSearchEditText.dismissDropDown()
            hideKeyboard()

            Toast.makeText(this, "Selected: $selectedSymptom", Toast.LENGTH_SHORT).show()
        }

        searchButton.setOnClickListener {
            val symptom = symptomSearchEditText.text.toString().trim()

            symptomSearchEditText.clearFocus()
            symptomSearchEditText.dismissDropDown()
            hideKeyboard()

            if (symptom.isEmpty()) {
                Toast.makeText(this, "Please enter a symptom", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Searching for: $symptom", Toast.LENGTH_SHORT).show()
            }
        }

        uploadReportButton.setOnClickListener {
            hideKeyboard()
            val intent = Intent(this, UploadReportActivity::class.java)
            startActivity(intent)
        }

        healthHistoryButton.setOnClickListener {
            hideKeyboard()
            val intent = Intent(this, HealthHistoryActivity::class.java)
            startActivity(intent)
        }

        recentActivityButton.setOnClickListener {
            hideKeyboard()
            val intent = Intent(this, RecentActivityActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        // Re-apply theme when returning from Settings page
        ThemeManager.applyTheme(this)
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