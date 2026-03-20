package com.example.mensajeria

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.util.Log

class CallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            if (state == TelephonyManager.EXTRA_STATE_RINGING) {
                val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
                
                val sharedPrefs = context.getSharedPreferences("AutoResponsePrefs", Context.MODE_PRIVATE)
                val targetNumber = sharedPrefs.getString("target_number", "")
                val responseMessage = sharedPrefs.getString("response_message", "")

                if (!targetNumber.isNullOrEmpty() && incomingNumber == targetNumber && !responseMessage.isNullOrEmpty()) {
                    sendSMS(targetNumber, responseMessage)
                }
            }
        }
    }

    private fun sendSMS(phoneNumber: String, message: String) {
        try {
            val smsManager: SmsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            Log.d("CallReceiver", "SMS sent to $phoneNumber")
        } catch (e: Exception) {
            Log.e("CallReceiver", "Failed to send SMS", e)
        }
    }
}
