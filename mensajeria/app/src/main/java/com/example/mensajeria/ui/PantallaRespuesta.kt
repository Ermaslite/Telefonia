package com.example.mensajeria.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun PantallaRespuesta(vistaModelo: VistaModeloRespuesta, modifier: Modifier = Modifier) {
    val contexto = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Configurar Respuesta Automática", style = MaterialTheme.typography.headlineSmall)
        
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = vistaModelo.numeroObjetivo,
            onValueChange = { vistaModelo.actualizarNumero(it) },
            label = { Text("Número de teléfono (ej: +52...)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = vistaModelo.mensajeRespuesta,
            onValueChange = { vistaModelo.actualizarMensaje(it) },
            label = { Text("Mensaje de respuesta") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                vistaModelo.guardarConfiguracion()
                Toast.makeText(contexto, "Configuración guardada", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Configuración")
        }
    }
}
