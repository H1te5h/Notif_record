package com.example.myapplication.utils

import android.annotation.SuppressLint
import android.content.Context

object AppExclusionManager {

    private const val PREFS_NAME = "excluded_apps"
    private const val EXCLUDED_SET_KEY = "excluded_set" 

    fun isAppExcluded(context: Context, packageName: String): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val excludedSet = prefs.getStringSet(EXCLUDED_SET_KEY, emptySet()) ?: emptySet()
        return excludedSet.contains(packageName)
    }

    @SuppressLint("ApplySharedPref", "UseKtx")
    fun toggleAppExclusion(context: Context, packageName: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val currentExclusions = prefs.getStringSet(EXCLUDED_SET_KEY, mutableSetOf())?.toMutableSet() ?: mutableSetOf()

        if (currentExclusions.contains(packageName)) {
            currentExclusions.remove(packageName)
        } else {
            currentExclusions.add(packageName)
        }
        prefs.edit().putStringSet(EXCLUDED_SET_KEY, currentExclusions).commit() // Using commit for immediate effect
    }

    fun getAllExcludedApps(context: Context): Set<String> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getStringSet(EXCLUDED_SET_KEY, emptySet()) ?: emptySet()
    }

}

