package com.example.myapplication.utils

import android.content.Context

import com.example.myapplication.data.NotificationRecord

import com.google.gson.GsonBuilder

import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object NotificationExporter {

    fun export(context: Context, records: List<NotificationRecord>): File {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val exportableRecords = records.map { record ->
            mapOf(
                "id" to record.id,
                "packageName" to record.packageName,
                "title" to record.title,
                "text" to record.content,
                "dateTime" to record.dateTime?.let { formatDate(it) }
            )
        }

        val json = gson.toJson(exportableRecords)

        val file = File(context.getExternalFilesDir(null), "notifications_export.json")
        file.writeText(json)
        return file
    }

    private fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm:ss", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}