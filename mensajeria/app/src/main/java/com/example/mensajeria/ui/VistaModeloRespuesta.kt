package com.example.mensajeria.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mensajeria.data.GestorPreferencias

class VistaModeloRespuesta(private val gestorPreferencias: GestorPreferencias) : ViewModel() {
    var numeroObjetivo by mutableStateOf(gestorPreferencias.obtenerNumeroObjetivo())
        private set

    var mensajeRespuesta by mutableStateOf(gestorPreferencias.obtenerMensajeRespuesta())
        private set

    fun actualizarNumero(nuevoNumero: String) {
        numeroObjetivo = nuevoNumero
    }

    fun actualizarMensaje(nuevoMensaje: String) {
        mensajeRespuesta = nuevoMensaje
    }

    fun guardarConfiguracion() {
        gestorPreferencias.guardarConfiguracion(numeroObjetivo, mensajeRespuesta)
    }
}
