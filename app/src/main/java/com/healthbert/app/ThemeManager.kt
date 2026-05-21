package com.healthbert.app

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

object ThemeManager {

    private const val SETTINGS_PREFS = "HealthBERTSettings"

    private val lightBackground = Color.parseColor("#F4F8FB")
    private val lightCard = Color.parseColor("#FFFFFF")
    private val lightText = Color.parseColor("#1E293B")
    private val lightSecondaryText = Color.parseColor("#64748B")
    private val lightButton = Color.parseColor("#D6D6D6")

    private val darkBackground = Color.parseColor("#111827")
    private val darkCard = Color.parseColor("#1F2937")
    private val darkInput = Color.parseColor("#374151")
    private val darkText = Color.parseColor("#F9FAFB")
    private val darkSecondaryText = Color.parseColor("#CBD5E1")

    private val primaryBlue = Color.parseColor("#2563EB")
    private val dangerRed = Color.parseColor("#DC2626")
    private val purpleStatus = Color.parseColor("#6D52AE")

    fun isDarkMode(context: Context): Boolean {
        val prefs = context.getSharedPreferences(SETTINGS_PREFS, Context.MODE_PRIVATE)
        return prefs.getBoolean("darkMode", false)
    }

    fun setDarkMode(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences(SETTINGS_PREFS, Context.MODE_PRIVATE)
        prefs.edit().putBoolean("darkMode", enabled).apply()
    }

    fun applyTheme(activity: Activity) {
        val isDark = isDarkMode(activity)

        activity.window.statusBarColor = purpleStatus
        activity.window.navigationBarColor = if (isDark) darkBackground else Color.WHITE

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = activity.window.decorView.systemUiVisibility

            flags = if (isDark) {
                flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            } else {
                flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }

            activity.window.decorView.systemUiVisibility = flags
        }

        val contentView = activity.findViewById<ViewGroup>(android.R.id.content)
        contentView.setBackgroundColor(if (isDark) darkBackground else lightBackground)

        applyThemeToView(contentView, isDark)
    }

    private fun applyThemeToView(view: View, isDark: Boolean) {
        when (view) {
            is ScrollView -> {
                view.setBackgroundColor(if (isDark) darkBackground else lightBackground)
            }

            is AutoCompleteTextView -> {
                view.setTextColor(if (isDark) darkText else lightText)
                view.setHintTextColor(if (isDark) darkSecondaryText else Color.parseColor("#94A3B8"))
                view.backgroundTintList =
                    ColorStateList.valueOf(if (isDark) darkSecondaryText else lightText)
            }

            is EditText -> {
                view.setTextColor(if (isDark) darkText else lightText)
                view.setHintTextColor(if (isDark) darkSecondaryText else Color.parseColor("#94A3B8"))
                view.backgroundTintList =
                    ColorStateList.valueOf(if (isDark) darkSecondaryText else lightText)
            }

            is Button -> {
                val buttonText = view.text.toString()

                when {
                    buttonText.equals("Logout", ignoreCase = true) -> {
                        view.backgroundTintList = ColorStateList.valueOf(dangerRed)
                        view.setTextColor(Color.WHITE)
                    }

                    buttonText.contains("Start Review Analysis", ignoreCase = true) ||
                            buttonText.contains("Save", ignoreCase = true) ||
                            buttonText.contains("Analyze", ignoreCase = true) -> {
                        view.backgroundTintList = ColorStateList.valueOf(primaryBlue)
                        view.setTextColor(Color.WHITE)
                    }

                    else -> {
                        view.backgroundTintList =
                            ColorStateList.valueOf(if (isDark) darkInput else lightButton)
                        view.setTextColor(if (isDark) darkText else Color.BLACK)
                    }
                }
            }

            is TextView -> {
                val text = view.text.toString()

                if (text.contains("Main Feature", ignoreCase = true) ||
                    text.contains("Back to Dashboard", ignoreCase = true)
                ) {
                    view.setTextColor(primaryBlue)
                } else {
                    view.setTextColor(if (isDark) darkText else lightText)
                }

                updateBackgroundIfNeeded(view, isDark)
            }

            is LinearLayout -> {
                updateBackgroundIfNeeded(view, isDark)
            }

            else -> {
                updateBackgroundIfNeeded(view, isDark)
            }
        }

        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                applyThemeToView(view.getChildAt(i), isDark)
            }
        }
    }

    private fun updateBackgroundIfNeeded(view: View, isDark: Boolean) {
        val background = view.background

        if (background is ColorDrawable) {
            val currentColor = background.color

            if (isDark) {
                when (currentColor) {
                    Color.WHITE, lightCard -> view.setBackgroundColor(darkCard)
                    lightBackground -> view.setBackgroundColor(darkBackground)
                }
            } else {
                when (currentColor) {
                    darkCard -> view.setBackgroundColor(lightCard)
                    darkBackground -> view.setBackgroundColor(lightBackground)
                    darkInput -> view.setBackgroundColor(lightCard)
                }
            }
        }
    }
}