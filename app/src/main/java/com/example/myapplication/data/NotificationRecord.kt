package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index
@Entity(
    tableName = "notifications",
    indices = [Index(value = ["title", "content", "packageName"], unique = true)]
)
data class NotificationRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val packageName: String?,
    val title: String?,
    val content: String?,
    val dateTime: Long?
)
