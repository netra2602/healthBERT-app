package com.healthbert.app

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

data class DemoUser(
    val name: String,
    val email: String,
    val password: String
)

object AuthManager {

    private const val PREF_NAME = "HealthBERTAuth"
    private const val KEY_USERS = "users"

    private val defaultUsers = listOf(
        DemoUser("Sarthak Matte", "sarthakmatte@healthbert.com", "sarthak123"),
        DemoUser("Doctor User", "doctor@healthbert.com", "doctor123"),
        DemoUser("Patient User", "patient@healthbert.com", "patient123")
    )

    fun seedDefaultUsers(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        if (!prefs.contains(KEY_USERS)) {
            saveUsers(context, defaultUsers.toMutableList())
        }
    }

    fun login(context: Context, email: String, password: String): Boolean {
        seedDefaultUsers(context)

        val users = getUsers(context)

        return users.any {
            it.email.equals(email, ignoreCase = true) && it.password == password
        }
    }

    fun signup(context: Context, name: String, email: String, password: String): Pair<Boolean, String> {
        seedDefaultUsers(context)

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return Pair(false, "Please fill all fields")
        }

        if (password.length < 6) {
            return Pair(false, "Password must be at least 6 characters")
        }

        val users = getUsers(context)

        val userExists = users.any {
            it.email.equals(email, ignoreCase = true)
        }

        if (userExists) {
            return Pair(false, "User already exists")
        }

        users.add(DemoUser(name, email, password))
        saveUsers(context, users)

        return Pair(true, "Signup successful")
    }

    private fun getUsers(context: Context): MutableList<DemoUser> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val usersJson = prefs.getString(KEY_USERS, "[]")

        val users = mutableListOf<DemoUser>()
        val jsonArray = JSONArray(usersJson)

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)

            users.add(
                DemoUser(
                    name = obj.getString("name"),
                    email = obj.getString("email"),
                    password = obj.getString("password")
                )
            )
        }

        return users
    }

    private fun saveUsers(context: Context, users: MutableList<DemoUser>) {
        val jsonArray = JSONArray()

        for (user in users) {
            val obj = JSONObject()
            obj.put("name", user.name)
            obj.put("email", user.email)
            obj.put("password", user.password)
            jsonArray.put(obj)
        }

        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        prefs.edit()
            .putString(KEY_USERS, jsonArray.toString())
            .apply()
    }
}