package com.alijafari.brik.main.presentation

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.preference.CheckBoxPreference
import androidx.preference.PreferenceFragmentCompat
import com.alijafari.brik.R
import com.alijafari.brik.block.framework.AdminManagerReceiver
import com.alijafari.brik.utils.PermissionUtils

class PermissionsFragment : PreferenceFragmentCompat() {
    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->}
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val overlayPref = findPreference<CheckBoxPreference>("overlay_permission")
        val deviceAdminPref = findPreference<CheckBoxPreference>("device_admin_permission")
        val notificationPref = findPreference<CheckBoxPreference>("post_notification_permission")

        overlayPref?.apply {
            if (isChecked) {
                setOnPreferenceClickListener{false}
                return
            }
            isChecked = Settings.canDrawOverlays(requireContext())
            setOnPreferenceClickListener {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    "package:${requireContext().packageName}".toUri()
                )
                startActivity(intent)
                false // prevent direct toggle
            }
        }

        deviceAdminPref?.apply {
            if (isChecked) {
                setOnPreferenceClickListener{false}
                return
            }
            val dpm =
                requireContext().getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
            val comp = ComponentName(requireContext(), AdminManagerReceiver::class.java)
            isChecked = dpm.isAdminActive(comp)
            setOnPreferenceClickListener {
                val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
                    putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, comp)
                }
                startActivity(intent)
                false
            }
        }

        notificationPref?.apply {
            if (isChecked) {
                setOnPreferenceClickListener{false}
                return
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                isChecked =
                    requireContext().checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
                setOnPreferenceClickListener {
                    permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                    false
                }
            }
            isChecked = true
        }

    }

    override fun onResume() {
        super.onResume()
        findPreference<CheckBoxPreference>("overlay_permission")?.isChecked =
            Settings.canDrawOverlays(requireContext())

        val dpm =
            requireContext().getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val comp = ComponentName(requireContext(), AdminManagerReceiver::class.java)
        findPreference<CheckBoxPreference>("device_admin_permission")?.isChecked =
            dpm.isAdminActive(comp)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            findPreference<CheckBoxPreference>("post_notification_permission")?.isChecked =
                requireContext().checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        }

        if (PermissionUtils.needsPermissionScreen(requireContext()).not()) requireActivity().finish()
    }
}

class PermissionCheckActivity() : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.permission_check_activity)
        supportFragmentManager.beginTransaction()
            .replace(R.id.content, PermissionsFragment())
            .commit()
    }
}