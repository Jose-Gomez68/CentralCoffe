package com.example.salestapapp.util

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesUtil(context: Context) {

    // Nombre del archivo de preferencias
    private val PREF_NAME = "dbPref"
    private val KEY_USER_ID = "userId"
    private val KEY_USER_NAME = "userName"
    private val KEY_NAME = "name"
    private val KEY_TYPE_USER = "userType"

    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = prefs.edit()

    /**
     * Guardar información de sesión
     */
    fun saveSession(userId: Int, userName: String, name: String) {
        editor.putInt(KEY_USER_ID, userId)
        editor.putString(KEY_USER_NAME, userName)
        editor.putString(KEY_NAME, name)
        editor.apply()
    }

    /**
     * Obtener ID de usuario
     */
    fun getUserId(): Int {
        return prefs.getInt(KEY_USER_ID, 0)
    }

    /**
     * Obtener nombre de usuario
     */
    fun getUserName(): String? {
        return prefs.getString(KEY_USER_NAME, null)
    }

    /**
     * Obtener nombre
     */
    fun getName(): String? {
        return prefs.getString(KEY_NAME, null)
    }

    /**
     * Obtener tipo de usuario: admin, vendedor, etc
     */
    fun getUserType(): String? {
        return prefs.getString(KEY_TYPE_USER, null)
    }

    /**
     * Limpiar el preferences
     */
    fun clearPref() {
        editor.clear()
        editor.apply()
    }

}