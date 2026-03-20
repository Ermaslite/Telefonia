package com.example.mensajeria

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mensajeria.data.GestorPreferencias
import com.example.mensajeria.ui.PantallaRespuesta
import com.example.mensajeria.ui.VistaModeloRespuesta
import com.example.mensajeria.ui.FabricaVistaModelo
import com.example.mensajeria.ui.theme.MensajeriaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MensajeriaTheme {
                val contexto = LocalContext.current
                val gestorPreferencias = remember { GestorPreferencias(contexto) }
                val vistaModelo: VistaModeloRespuesta = viewModel(
                    factory = FabricaVistaModelo(gestorPreferencias)
                )

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PantallaPrincipal(
                        vistaModelo = vistaModelo,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun PantallaPrincipal(vistaModelo: VistaModeloRespuesta, modifier: Modifier = Modifier) {
    val contexto = LocalContext.current
    val permisos = arrayOf(
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.READ_CALL_LOG,
        Manifest.permission.SEND_SMS
    )

    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { mapaPermisos ->
        val todosConcedidos = mapaPermisos.values.all { it }
        if (!todosConcedidos) {
            Toast.makeText(contexto, "Se requieren permisos para funcionar", Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(Unit) {
        val necesitaPermisos = permisos.any {
            ContextCompat.checkSelfPermission(contexto, it) != PackageManager.PERMISSION_GRANTED
        }
        if (necesitaPermisos) {
            lanzador.launch(permisos)
        }
    }

    PantallaRespuesta(vistaModelo = vistaModelo, modifier = modifier)
}
