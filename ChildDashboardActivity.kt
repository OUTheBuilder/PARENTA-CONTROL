package com.parentguard.ui.child

import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.parentguard.databinding.ActivityChildDashboardBinding
import com.parentguard.service.MonitoringService
import com.parentguard.util.DeviceControlHelper

/**
 * This screen is the transparency point: it tells the device user exactly
 * what's being turned on and why, and only starts monitoring after they
 * tap through each permission themselves. Don't remove these explanations
 * or try to pre-grant permissions silently — besides being against policy,
 * several of these (Device Admin, Usage Access) cannot be granted
 * programmatically on modern Android regardless.
 */
class ChildDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChildDashboardBinding
    private lateinit var deviceControl: DeviceControlHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChildDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        deviceControl = DeviceControlHelper(this)

        binding.enableDeviceAdminBtn.setOnClickListener { requestDeviceAdmin() }
        binding.enableUsageAccessBtn.setOnClickListener {
            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }
        binding.startMonitoringBtn.setOnClickListener { startMonitoringService() }

        refreshStatus()
    }

    override fun onResume() {
        super.onResume()
        refreshStatus()
    }

    private fun refreshStatus() {
        binding.deviceAdminStatus.text =
            if (deviceControl.isAdminActive()) "Device admin: ON" else "Device admin: off"
    }

    private fun requestDeviceAdmin() {
        val intent = Intent(android.app.admin.DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
            putExtra(android.app.admin.DevicePolicyManager.EXTRA_DEVICE_ADMIN, deviceControl.adminComponentName())
            putExtra(
                android.app.admin.DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "ParentGuard uses this to let a parent lock this device remotely."
            )
        }
        startActivity(intent)
    }

    private fun startMonitoringService() {
        val intent = Intent(this, MonitoringService::class.java)
        ContextCompat.startForegroundService(this, intent)
    }
}
