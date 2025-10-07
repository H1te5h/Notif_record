package com.example.myapplication.utils

import android.service.notification.StatusBarNotification
import android.util.Log
object NotificationLogger {

    private const val TAG = "NotificationListener"

    fun logConnectionStatus(isConnected: Boolean) {
        if (isConnected) {
            Log.d(TAG, "Notification Listener connected.")
        } else {
            Log.w(TAG, "Notification Listener disconnected.")
        }
    }

    fun logPosted(sbn: StatusBarNotification) {
        val title = sbn.notification.extras.getString(android.app.Notification.EXTRA_TITLE)
        Log.i(TAG, "--- NOTIFICATION POSTED ---")
        Log.i(TAG, "Package: ${sbn.packageName}")
        Log.i(TAG, "Title: $title")
        Log.i(TAG, "ID: ${sbn.id}")
        Log.i(TAG, "---------------------------")
    }

    fun logRemoved(sbn: StatusBarNotification) {
        val title = sbn.notification.extras.getString(android.app.Notification.EXTRA_TITLE)
        Log.i(TAG, "--- NOTIFICATION REMOVED ---")
        Log.i(TAG, "Package: ${sbn.packageName}")
        Log.i(TAG, "Title: $title")
        Log.i(TAG, "ID: ${sbn.id}")
        Log.i(TAG, "----------------------------")
    }
}
