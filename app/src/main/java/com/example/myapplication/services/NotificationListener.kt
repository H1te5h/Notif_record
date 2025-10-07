
package com.example.myapplication.services

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

import com.example.myapplication.data.NotificationDatabase
import com.example.myapplication.data.NotificationRecord
import com.example.myapplication.handlers.NotificationHandler
import com.example.myapplication.utils.NotificationLogger

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class NotificationListener : NotificationListenerService() {

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val database by lazy { NotificationDatabase.getDatabase(applicationContext) }
    private var lastNotification: NotificationRecord? = null
    private val notificationInterval = 1000
    private val notificationHandler by lazy { NotificationHandler(applicationContext, serviceScope, database.notificationDao()) }

    override fun onListenerConnected() {
        super.onListenerConnected()
        NotificationLogger.logConnectionStatus(true)
    }
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        sbn?.notification?.let { notification ->
            val packageName = sbn.packageName ?: "Unknown"
            val title = notification.extras.getString("android.title") ?: ""
            val content = notification.extras.getString("android.text") ?: ""
            val postTime = sbn.postTime

            if (title.isBlank() && content.isBlank()) {
                return
            }
            val newRecord = NotificationRecord(
                packageName = packageName,
                title = title,
                content = content,
                dateTime = postTime
            )

            lastNotification?.let { last ->
                val timeDifference = kotlin.math.abs(newRecord.dateTime!! - last.dateTime!!)
                if (last.title == newRecord.title &&
                    last.packageName == newRecord.packageName &&
                    timeDifference < notificationInterval) {
                    return
                }
            }

            lastNotification = newRecord
            serviceScope.launch {
                database.notificationDao().insert(newRecord)
            }

        }
    }
    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        sbn?.let {
            NotificationLogger.logRemoved(it)
            notificationHandler.processRemovedNotification(it)
        }
    }
    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        NotificationLogger.logConnectionStatus(false)
    }
    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}
