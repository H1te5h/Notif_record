package com.example.myapplication.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy

@Dao
interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(record: NotificationRecord): Long

    @Query("SELECT * FROM notifications ORDER BY dateTime DESC")
    fun getAll(): LiveData<List<NotificationRecord>>

    @Query("SELECT * FROM notifications ORDER BY dateTime DESC")
    suspend fun getAllStatic(): List<NotificationRecord>
}
