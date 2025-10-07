package com.example.myapplication.ui.notificationViewingUI

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.example.myapplication.R
import com.example.myapplication.data.NotificationRecord

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificationAdapter :
    ListAdapter<NotificationRecord, NotificationAdapter.NotificationViewHolder>(NotificationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val record = getItem(position)
        holder.bind(record)
    }

    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.notification_title)
        private val content: TextView = itemView.findViewById(R.id.notification_content)
        private val timestamp: TextView = itemView.findViewById(R.id.notification_date_and_time)

        fun bind(record: NotificationRecord) {
            title.text = record.title
            content.text = record.content ?: "No Content"
            timestamp.text = formatDate(record.dateTime)
        }
    }

    private fun formatDate(timestamp: Long?): String {
        if (timestamp == null) return "Unknown"
        val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}

class NotificationDiffCallback : DiffUtil.ItemCallback<NotificationRecord>() {
    override fun areItemsTheSame(oldItem: NotificationRecord, newItem: NotificationRecord): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: NotificationRecord, newItem: NotificationRecord): Boolean {
        return oldItem == newItem
    }
}