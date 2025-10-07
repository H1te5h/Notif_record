package com.example.myapplication.handlers

import android.app.Notification
import android.content.Context
import android.service.notification.StatusBarNotification

import com.example.myapplication.data.NotificationDao
import com.example.myapplication.data.NotificationRecord
import com.example.myapplication.utils.AppExclusionManager

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationHandler(
    private val context: Context,
    private val scope: CoroutineScope,
    private val notificationDao: NotificationDao
) {

    fun processPostedNotification(sbn: StatusBarNotification) {
        val packageName = sbn.packageName

        if (AppExclusionManager.isAppExcluded(context, packageName)) {
            return
        }

        val extras = sbn.notification.extras

        val title = extras.getCharSequence(Notification.EXTRA_TITLE)?.toString()
        val text = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString()
        val dateTimE: Long = sbn.postTime

        if (title.isNullOrBlank() && text.isNullOrBlank()) {
            return
        }

        val record = NotificationRecord(
            packageName = packageName,
            title = title,
            content = text,
            dateTime = dateTimE
        )

        scope.launch(Dispatchers.IO) {
            notificationDao.insert(record)
        }
    }

    fun processRemovedNotification(sbn: StatusBarNotification) {
        val notificationId = sbn.id
        val packageName = sbn.packageName
        // scope.launch(Dispatchers.IO) {
        //     notificationDao.updateStatusToRemoved(packageName, notificationId)
        // }
    }
}
