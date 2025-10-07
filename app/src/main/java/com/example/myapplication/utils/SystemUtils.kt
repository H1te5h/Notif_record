package com.example.myapplication.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.widget.Toast

import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri

import com.example.myapplication.R

object SystemUtils {

    fun isNotificationListenerEnabled(context: Context): Boolean {
        val enabledListeners = NotificationManagerCompat.getEnabledListenerPackages(context)
        return enabledListeners.contains(context.packageName)
    }

    fun requestNotificationAccessPermission(activity: Activity) {
        try {
            val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
            activity.startActivity(intent)
            Toast.makeText(
                activity,
                "Please find '${activity.getString(R.string.app_name)}' and grant Notification Access.",
                Toast.LENGTH_LONG
            ).show()
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                activity,
                "Could not open Notification Access settings. Please find it manually in system settings.",
                Toast.LENGTH_LONG
            ).show()
            e.printStackTrace()
        } catch (e: Exception) {
            Toast.makeText(activity, "Error opening Notification Access settings.", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    @SuppressLint("ObsoleteSdkInt", "BatteryLife")
    fun requestDisableBatteryOptimization(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val packageName = activity.packageName
            val powerManager = activity.getSystemService(Context.POWER_SERVICE) as? PowerManager

            if (powerManager == null) {
                Toast.makeText(activity, "Could not access PowerManager.", Toast.LENGTH_SHORT).show()
                return
            }

            if (!powerManager.isIgnoringBatteryOptimizations(packageName)) {
                try {
                    val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                    intent.data = "package:$packageName".toUri()
                    activity.startActivity(intent)
                    Toast.makeText(activity, "Please allow the app to run without battery restrictions.", Toast.LENGTH_LONG).show()
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(activity, "Could not open battery optimization settings. Please adjust manually.", Toast.LENGTH_LONG).show()
                    e.printStackTrace()
                } catch (e: Exception) {
                    Toast.makeText(activity, "Error opening battery settings.", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(activity, "Battery optimization is already disabled for this app.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(activity, "Battery optimization settings are not applicable on this Android version.", Toast.LENGTH_SHORT).show()
        }
    }


    @SuppressLint("ObsoleteSdkInt")
    fun isBatteryOptimizationDisabled(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val packageName = context.packageName
            val powerManager = context.getSystemService(Context.POWER_SERVICE) as? PowerManager
            return powerManager?.isIgnoringBatteryOptimizations(packageName) ?: false
        }
        return true
    }
}
