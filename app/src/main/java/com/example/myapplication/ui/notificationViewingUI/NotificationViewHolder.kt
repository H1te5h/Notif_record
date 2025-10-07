package com.example.myapplication.ui.notificationViewingUI

import android.view.View
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView

import com.example.myapplication.R
import com.example.myapplication.data.NotificationRecord

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date

class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val title: TextView = itemView.findViewById(R.id.notification_title)
    val content: TextView = itemView.findViewById(R.id.notification_content)
    val timestamp: TextView = itemView.findViewById(R.id.notification_date_and_time)

    fun bind(record: NotificationRecord) {
        title.text = record.title
        content.text = record.content ?: "No Content" // Corrected property access
        timestamp.text = record.dateTime?.let { formatDate(it) } ?: "No Date"
    }

    private fun formatDate(dateTime: Long): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date(dateTime))
    }
}