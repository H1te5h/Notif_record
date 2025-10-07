package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar

import com.example.myapplication.services.NotificationListener
import com.example.myapplication.ui.exclusion.AppExclusionActivity
import com.example.myapplication.utils.SystemUtils

class MainActivity : AppCompatActivity() {

    private lateinit var recordNotificationContainer: LinearLayout
    private lateinit var recordNotificationSwitch: SwitchCompat
    private lateinit var viewNotificationHistory: LinearLayout
    private lateinit var manageExclusions: LinearLayout
    private lateinit var disableBatteryOptimization: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        recordNotificationContainer = findViewById(R.id.record_notification_container)
        recordNotificationSwitch = findViewById(R.id.record_notification_switch)
        viewNotificationHistory = findViewById(R.id.view_notification_history)
        manageExclusions = findViewById(R.id.manage_exclusions)
        disableBatteryOptimization = findViewById(R.id.disable_battery_optimization)

        updateSwitchState()

        recordNotificationContainer.setOnClickListener {
            recordNotificationSwitch.toggle()
        }
        recordNotificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (SystemUtils.isNotificationListenerEnabled(this)) {
                    startNotificationService()
                } else {
                    Toast.makeText(
                        this,
                        "Notification access permission required.",
                        Toast.LENGTH_LONG
                    ).show()
                    SystemUtils.requestNotificationAccessPermission(this)
                    recordNotificationSwitch.isChecked = false
                }
            } else {
                stopNotificationService()
            }
        }

        viewNotificationHistory.setOnClickListener {
            startActivity(Intent(this, NotificationListActivity::class.java))
        }

        manageExclusions.setOnClickListener {
            val intent = Intent(this, AppExclusionActivity::class.java)
            startActivity(intent)
        }

        disableBatteryOptimization.setOnClickListener {
            SystemUtils.requestDisableBatteryOptimization(this)
        }
    }

    private fun startNotificationService() {
        val intent = Intent(this, NotificationListener::class.java)
        try {
            startService(intent)
            Toast.makeText(this, "Notification recording started", Toast.LENGTH_SHORT).show()
            Log.d("MainActivity", "Notification service started.")
        } catch (e: IllegalStateException) {
            Log.e("MainActivity", "Failed to start notification service: ${e.message}")
            Toast.makeText(
                this,
                "Failed to start recording. Ensure app can run in background.",
                Toast.LENGTH_LONG
            ).show()
            recordNotificationSwitch.isChecked = false
        }
    }

    private fun stopNotificationService() {
        val intent = Intent(this, NotificationListener::class.java)
        stopService(intent)
        Toast.makeText(this, "Notification recording stopped", Toast.LENGTH_SHORT).show()
        Log.d("MainActivity", "Notification service stopped.")
    }

    private fun updateSwitchState() {
        if (SystemUtils.isNotificationListenerEnabled(this)) {
            val sharedPrefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
            recordNotificationSwitch.isChecked = sharedPrefs.getBoolean(
                "notification_service_active", false
            ) && SystemUtils.isNotificationListenerEnabled(this)

        } else {
            recordNotificationSwitch.isChecked = false
        }
    }

    override fun onResume() {
        super.onResume()
        updateSwitchState()
    }

    @SuppressLint("UseKtx")
    override fun onPause() {
        super.onPause()
        val sharedPrefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        sharedPrefs.edit().putBoolean("notification_service_active", recordNotificationSwitch.isChecked).apply()
    }
}
