package com.example.myapplication.ui.exclusion

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView

import com.example.myapplication.databinding.ItemExclusionAppBinding
import com.example.myapplication.utils.AppExclusionManager
import com.example.myapplication.data.AppInfo

class ExclusionAppAdapter(
    private val context: Context,
    private var appList: List<AppInfo>,
    private var excludedApps: MutableSet<String>
) : RecyclerView.Adapter<ExclusionAppAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemExclusionAppBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(appInfo: AppInfo) {
            binding.tvAppName.text = appInfo.appName
            binding.ivAppIcon.setImageDrawable(appInfo.icon)
            binding.switchAppExcluded.isChecked = excludedApps.contains(appInfo.packageName)

            binding.switchAppExcluded.setOnCheckedChangeListener(null) // Important to prevent listener firing during bind
            binding.switchAppExcluded.isChecked = excludedApps.contains(appInfo.packageName)

            binding.switchAppExcluded.setOnCheckedChangeListener { _, isChecked ->
                AppExclusionManager.toggleAppExclusion(context, appInfo.packageName)
                if (isChecked) {
                    excludedApps.add(appInfo.packageName)
                } else {
                    excludedApps.remove(appInfo.packageName)
                }
                // No need to notifyDataSetChanged here for a single item usually,
                // but good if the underlying data in AppExclusionManager needs to be fully re-read by others.
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemExclusionAppBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(appList[position])
    }

    override fun getItemCount(): Int = appList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newAppList: List<AppInfo>, newExcludedApps: Set<String>) {
        this.appList = newAppList
        this.excludedApps = newExcludedApps.toMutableSet()
        notifyDataSetChanged() // Consider using DiffUtil for better performance
    }
}
