package com.alijafari.brik.utils

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat
import com.alijafari.brik.block.framework.AdminManagerReceiver

object PermissionUtils {
    fun needsPermissionScreen(context: Context): Boolean {
        // Overlay
        if (!Settings.canDrawOverlays(context)) return true

        // Device Admin
        val dpm = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val comp = ComponentName(context, AdminManagerReceiver::class.java)
        if (!dpm.isAdminActive(comp)) return true

        // Notifications (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!granted) return true
        }
        return false
    }
}
