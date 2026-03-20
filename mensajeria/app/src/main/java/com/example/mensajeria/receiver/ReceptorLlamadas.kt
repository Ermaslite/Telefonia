package com.example.mensajeria.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.util.Log
import com.example.mensajeria.data.GestorPreferencias

class ReceptorLlamadas : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val estado = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            val gestor = GestorPreferencias(context)
            
            Log.d("RespuestaAuto", "Cambio de estado detectado: $estado")

            when (estado) {
                TelephonyManager.EXTRA_STATE_RINGING -> {
                    // 1. Cuando suena, capturamos el número
                    val numeroEntrante = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
                    if (numeroEntrante != null) {
                        gestor.guardarNumeroTemporal(numeroEntrante)
                        Log.d("RespuestaAuto", "Llamada entrante: $numeroEntrante. Guardando para responder después.")
                    } else {
                        Log.e("RespuestaAuto", "Número entrante es NULL. Verifica permisos de READ_CALL_LOG.")
                    }
                }

                TelephonyManager.EXTRA_STATE_OFFHOOK -> {
                    // 2. Si contestas, cancelamos el envío
                    gestor.limpiarNumeroTemporal()
                    Log.d("RespuestaAuto", "Llamada contestada. Cancelando respuesta.")
                }

                TelephonyManager.EXTRA_STATE_IDLE -> {
                    // 3. Cuando cuelgas o no contestas, verificamos si hay que enviar SMS
                    val numeroPendiente = gestor.obtenerNumeroTemporal()
                    val numeroConfigurado = gestor.obtenerNumeroObjetivo()
                    val mensaje = gestor.obtenerMensajeRespuesta()

                    if (numeroPendiente.isNotEmpty() && numeroConfigurado.isNotEmpty() && mensaje.isNotEmpty()) {
                        val numEntranteLimpio = numeroPendiente.replace(Regex("[^0-9]"), "")
                        val numConfigLimpio = numeroConfigurado.replace(Regex("[^0-9]"), "")

                        // Comparamos los últimos 10 dígitos
                        if (numEntranteLimpio.endsWith(numConfigLimpio.takeLast(10))) {
                            Log.d("RespuestaAuto", "¡Coincidencia! Enviando SMS a $numeroPendiente")
                            enviarSMS(context, numeroPendiente, mensaje)
                        } else {
                            Log.d("RespuestaAuto", "No coinciden: $numEntranteLimpio vs $numConfigLimpio")
                        }
                    }
                    gestor.limpiarNumeroTemporal()
                }
            }
        }
    }

    private fun enviarSMS(context: Context, numero: String, mensaje: String) {
        try {
            val smsManager: SmsManager = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                context.getSystemService(SmsManager::class.java)
            } else {
                @Suppress("DEPRECATION")
                SmsManager.getDefault()
            }
            smsManager.sendTextMessage(numero, null, mensaje, null, null)
            Log.d("RespuestaAuto", "SMS enviado exitosamente.")
        } catch (e: Exception) {
            Log.e("RespuestaAuto", "Error al enviar: ${e.message}")
        }
    }
}
