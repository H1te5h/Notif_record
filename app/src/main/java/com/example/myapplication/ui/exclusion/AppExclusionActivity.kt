package com.example.myapplication.ui.exclusion

import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.view.View

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.myapplication.databinding.ActivityAppExclusionBinding
import com.example.myapplication.utils.AppExclusionManager
import com.example.myapplication.data.AppInfo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppExclusionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppExclusionBinding
    private lateinit var adapter: ExclusionAppAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppExclusionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Manage Excluded Apps"

        setupRecyclerView()
        loadApps()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
    private fun setupRecyclerView() {
        adapter = ExclusionAppAdapter(this, emptyList(), mutableSetOf())
        binding.rvAppExclusions.layoutManager = LinearLayoutManager(this)
        binding.rvAppExclusions.adapter = adapter
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun loadApps() {
        binding.progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            val appList = withContext(Dispatchers.IO) {
                val pm = packageManager
                pm.getInstalledApplications(0)
                    .filter { it.flags and ApplicationInfo.FLAG_SYSTEM == 0 }
                    .map {
                        AppInfo(
                            appName = it.loadLabel(pm).toString(),
                            packageName = it.packageName,
                            icon = it.loadIcon(pm)
                        )
                    }
                    .sortedBy { it.appName.lowercase() }
            }
            val excludedApps = AppExclusionManager.getAllExcludedApps(this@AppExclusionActivity)
            adapter.updateData(appList, excludedApps)
            binding.progressBar.visibility = View.GONE
        }
    }
}