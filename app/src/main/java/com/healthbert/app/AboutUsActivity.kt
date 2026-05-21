package com.healthbert.app

import android.app.Activity
import android.os.Bundle
import android.widget.TextView

class AboutUsActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)

        ThemeManager.applyTheme(this)

        val backToDashboardButton = findViewById<TextView>(R.id.backToDashboardButton)

        backToDashboardButton.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        ThemeManager.applyTheme(this)
    }
}