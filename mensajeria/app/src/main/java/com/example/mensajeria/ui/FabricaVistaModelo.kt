package com.example.mensajeria.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mensajeria.data.GestorPreferencias

class FabricaVistaModelo(private val gestorPreferencias: GestorPreferencias) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VistaModeloRespuesta::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VistaModeloRespuesta(gestorPreferencias) as T
        }
        throw IllegalArgumentException("Clase ViewModel desconocida")
    }
}
