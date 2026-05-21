package com.healthbert.app

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast

class SettingsActivity : Activity() {

    private val settingsPrefs = "HealthBERTSettings"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        ThemeManager.applyTheme(this)

        val backToDashboardButton = findViewById<TextView>(R.id.backToDashboardButton)
        val darkModeSwitch = findViewById<Switch>(R.id.darkModeSwitch)
        val languageSpinner = findViewById<Spinner>(R.id.languageSpinner)
        val notificationSwitch = findViewById<Switch>(R.id.notificationSwitch)
        val privacySwitch = findViewById<Switch>(R.id.privacySwitch)
        val logoutButton = findViewById<Button>(R.id.logoutButton)

        val prefs = getSharedPreferences(settingsPrefs, Context.MODE_PRIVATE)

        val languages = listOf("English", "Hindi", "Marathi", "Spanish")

        val languageAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            languages
        )

        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        languageSpinner.adapter = languageAdapter

        val savedLanguage = prefs.getString("language", "English")
        val savedLanguageIndex = languages.indexOf(savedLanguage)

        if (savedLanguageIndex >= 0) {
            languageSpinner.setSelection(savedLanguageIndex)
        }

        darkModeSwitch.isChecked = ThemeManager.isDarkMode(this)
        notificationSwitch.isChecked = prefs.getBoolean("notifications", true)
        privacySwitch.isChecked = prefs.getBoolean("privacyMode", true)

        backToDashboardButton.setOnClickListener {
            saveSettings(
                prefs,
                languageSpinner,
                notificationSwitch,
                privacySwitch
            )
            finish()
        }

        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            ThemeManager.setDarkMode(this, isChecked)
            ThemeManager.applyTheme(this)

            if (isChecked) {
                Toast.makeText(this, "Dark mode enabled", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Dark mode disabled", Toast.LENGTH_SHORT).show()
            }
        }

        notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("notifications", isChecked).apply()

            if (isChecked) {
                Toast.makeText(this, "Notifications enabled", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notifications disabled", Toast.LENGTH_SHORT).show()
            }
        }

        privacySwitch.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("privacyMode", isChecked).apply()

            if (isChecked) {
                Toast.makeText(this, "Privacy mode enabled", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Privacy mode disabled", Toast.LENGTH_SHORT).show()
            }
        }

        logoutButton.setOnClickListener {
            saveSettings(
                prefs,
                languageSpinner,
                notificationSwitch,
                privacySwitch
            )

            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun saveSettings(
        prefs: android.content.SharedPreferences,
        languageSpinner: Spinner,
        notificationSwitch: Switch,
        privacySwitch: Switch
    ) {
        prefs.edit()
            .putString("language", languageSpinner.selectedItem.toString())
            .putBoolean("notifications", notificationSwitch.isChecked)
            .putBoolean("privacyMode", privacySwitch.isChecked)
            .apply()
    }
}