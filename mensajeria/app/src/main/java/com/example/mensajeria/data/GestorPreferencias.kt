package com.example.mensajeria.data

import android.content.Context
import android.content.SharedPreferences

class GestorPreferencias(context: Context) {
    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences("PreferenciasRespuestaAuto", Context.MODE_PRIVATE)

    fun guardarConfiguracion(numero: String, mensaje: String) {
        sharedPrefs.edit().apply {
            putString("numero_objetivo", numero)
            putString("mensaje_respuesta", mensaje)
            apply()
        }
    }

    fun obtenerNumeroObjetivo(): String = sharedPrefs.getString("numero_objetivo", "") ?: ""
    fun obtenerMensajeRespuesta(): String = sharedPrefs.getString("mensaje_respuesta", "") ?: ""

    // Funciones para el flujo de la llamada
    fun guardarNumeroTemporal(numero: String) {
        sharedPrefs.edit().putString("numero_temporal", numero).apply()
    }

    fun obtenerNumeroTemporal(): String = sharedPrefs.getString("numero_temporal", "") ?: ""

    fun limpiarNumeroTemporal() {
        sharedPrefs.edit().remove("numero_temporal").apply()
    }
}
