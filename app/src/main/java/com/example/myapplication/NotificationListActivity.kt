package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.Toast

import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.myapplication.data.NotificationDatabase
import com.example.myapplication.ui.notificationViewingUI.NotificationAdapter
import com.example.myapplication.utils.NotificationExporter

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class NotificationListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var exportButton: Button
    private var adapter = NotificationAdapter ()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_list)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Notification History"

        recyclerView = findViewById(R.id.recyclerView)
        exportButton = findViewById(R.id.exportButton)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val dao = NotificationDatabase.getDatabase(this).notificationDao()

        dao.getAll().observe(this) { notifications ->
            adapter.submitList(notifications)
        }

        exportButton.setOnClickListener {
            lifecycleScope.launch {
                val notifications = withContext(Dispatchers.IO) {
                    dao.getAllStatic()
                }
                val exportedFile = NotificationExporter.export(this@NotificationListActivity, notifications)
                Toast.makeText(this@NotificationListActivity, "Exported to: ${exportedFile.absolutePath}", Toast.LENGTH_LONG).show()
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
