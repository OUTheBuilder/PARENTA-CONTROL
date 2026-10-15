package com.parentguard.util

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import com.parentguard.admin.MyDeviceAdminReceiver

class DeviceControlHelper(private val context: Context) {
    private val dpm = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    private val adminComponent = ComponentName(context, MyDeviceAdminReceiver::class.java)

    fun isAdminActive(): Boolean = dpm.isAdminActive(adminComponent)

    fun lockDevice() {
        if (isAdminActive()) dpm.lockNow()
    }

    fun adminComponentName(): ComponentName = adminComponent
}
