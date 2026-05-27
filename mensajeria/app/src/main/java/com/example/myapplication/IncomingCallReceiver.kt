package com.example.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.util.Log

/**
 * BroadcastReceiver registrado en el AndroidManifest.xml que escucha
 * los cambios de estado telefónico (ACTION_PHONE_STATE_CHANGED).
 *
 * Cuando detecta una llamada entrante (CALL_STATE_RINGING) desde el número
 * configurado por el usuario, envía automáticamente un SMS de respuesta.
 *
 * Referencia:
 * https://developer.android.com/reference/android/telephony/TelephonyManager#ACTION_PHONE_STATE_CHANGED
 */
class IncomingCallReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "IncomingCallReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        // Solo procesamos el intent de cambio de estado telefónico
        if (intent.action != TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            return
        }

        // Verificar si la respuesta automática está habilitada
        if (!AutoReplyPreferences.isEnabled(context)) {
            Log.d(TAG, "Respuesta automática desactivada, ignorando llamada.")
            return
        }

        // Obtener el estado de la llamada
        val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
        // Obtener el número entrante
        val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

        Log.d(TAG, "Estado de llamada: $state, Número entrante: $incomingNumber")

        // Solo actuar cuando el teléfono está sonando (llamada entrante)
        if (state == TelephonyManager.EXTRA_STATE_RINGING && incomingNumber != null) {
            val targetNumber = AutoReplyPreferences.getPhoneNumber(context)
            val autoReplyMessage = AutoReplyPreferences.getMessage(context)

            Log.d(TAG, "Número configurado: $targetNumber")

            // Normalizar ambos números eliminando espacios, guiones y el prefijo '+'
            val normalizedIncoming = normalizePhoneNumber(incomingNumber)
            val normalizedTarget = normalizePhoneNumber(targetNumber)

            if (normalizedIncoming.isNotEmpty() && normalizedTarget.isNotEmpty()
                && normalizedIncoming.endsWith(normalizedTarget) || normalizedTarget.endsWith(normalizedIncoming)
            ) {
                if (autoReplyMessage.isNotEmpty()) {
                    sendSms(incomingNumber, autoReplyMessage)
                    Log.i(TAG, "SMS de respuesta automática enviado a $incomingNumber: $autoReplyMessage")
                } else {
                    Log.w(TAG, "No se ha configurado un mensaje de respuesta automática.")
                }
            } else {
                Log.d(TAG, "El número entrante ($normalizedIncoming) no coincide con el configurado ($normalizedTarget).")
            }
        }
    }

    /**
     * Normaliza un número de teléfono eliminando caracteres no numéricos
     * para comparación flexible.
     */
    private fun normalizePhoneNumber(number: String): String {
        return number.replace(Regex("[^0-9]"), "")
    }

    /**
     * Envía un SMS al número especificado con el mensaje dado.
     */
    private fun sendSms(phoneNumber: String, message: String) {
        try {
            val smsManager = SmsManager.getDefault()
            // Si el mensaje es muy largo, dividirlo en partes
            val parts = smsManager.divideMessage(message)
            if (parts.size == 1) {
                smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            } else {
                smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null, null)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error al enviar SMS: ${e.message}", e)
        }
    }
}
