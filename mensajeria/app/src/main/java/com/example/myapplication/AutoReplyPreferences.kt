package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences

/**
 * Helper class para almacenar y recuperar las preferencias de respuesta automática
 * usando SharedPreferences.
 */
object AutoReplyPreferences {

    private const val PREFS_NAME = "auto_reply_prefs"
    private const val KEY_PHONE_NUMBER = "phone_number"
    private const val KEY_MESSAGE = "auto_reply_message"
    private const val KEY_ENABLED = "auto_reply_enabled"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    /** Guarda el número de teléfono al que se responderá automáticamente */
    fun savePhoneNumber(context: Context, phoneNumber: String) {
        getPrefs(context).edit().putString(KEY_PHONE_NUMBER, phoneNumber).apply()
    }

    /** Obtiene el número de teléfono configurado */
    fun getPhoneNumber(context: Context): String {
        return getPrefs(context).getString(KEY_PHONE_NUMBER, "") ?: ""
    }

    /** Guarda el mensaje de respuesta automática */
    fun saveMessage(context: Context, message: String) {
        getPrefs(context).edit().putString(KEY_MESSAGE, message).apply()
    }

    /** Obtiene el mensaje de respuesta automática configurado */
    fun getMessage(context: Context): String {
        return getPrefs(context).getString(KEY_MESSAGE, "") ?: ""
    }

    /** Activa o desactiva la respuesta automática */
    fun setEnabled(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean(KEY_ENABLED, enabled).apply()
    }

    /** Verifica si la respuesta automática está habilitada */
    fun isEnabled(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_ENABLED, false)
    }
}
