package com.alijafari.brik.block.framework

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.alijafari.brik.R
import com.alijafari.brik.block.domain.BlockSessionManager
import com.alijafari.brik.block.helpers.OverlayManager
import com.alijafari.brik.block.presentation.BlockActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BlockService : Service(), BlockSessionManager {
    companion object {
        const val INTENT_START = "start"
        const val EXTRA_DURATION_SECONDS = "duration"
        const val NOTIFICATION_ID = 6969
        const val NOTIFICATION_CHANNEL_ID = "foreground_service_channel"
        const val TAG = "Block Service"
    }

    val binder = LocalBinder()

    inner class LocalBinder() : Binder() {
        fun getService(): BlockService = this@BlockService

    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    var sessionDurationSeconds = 0
        private set
    var sessionRemainingSeconds = sessionDurationSeconds
        private set

    val sessionTimer: SessionTimerImpl = SessionTimerImpl()
    override fun startSession() {
        sessionTimer.start(
            sessionDurationSeconds * 1000L
        )
        sessionTimer.addOnTickListener {
            update(it)
        }
        startBlockActivity()
    }

    private fun startBlockActivity() {
        val activityIntent = Intent(
            applicationContext, BlockActivity::class.java
        )
        activityIntent.addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK
        )
        startActivity(
            activityIntent
        )
    }


    override fun stopSession() {
        stopSelf()
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    override fun extend(extraMillis: Long) {
        sessionTimer.extend(extraMillis)
    }


    lateinit var notificationManager: NotificationManager


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null) throw NullPointerException()
        when (intent.action) {
            INTENT_START -> {
                notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                sessionDurationSeconds = intent.getIntExtra(EXTRA_DURATION_SECONDS, 60)
                sessionRemainingSeconds = sessionDurationSeconds

                OverlayManager(applicationContext).startOverlay()
                startSession()
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                NOTIFICATION_ID,
                getServiceNotification(),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MANIFEST
            )
        } else {
            startForeground(NOTIFICATION_ID, getServiceNotification())
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun update(remainingMillis: Long) {
        sessionRemainingSeconds = (remainingMillis / 1000).toInt()
        notificationManager.notify(
            NOTIFICATION_ID, getServiceNotification()
        )
    }

    private fun getServiceNotification(): Notification = getNotificationBuilder()
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Title")
        .setContentText("$sessionRemainingSeconds seconds remaining")
        .setSilent(true)
        .build()


    private fun getNotificationBuilder(): NotificationCompat.Builder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.buildChannel()
            NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
        } else {
            NotificationCompat.Builder(applicationContext)
        }
    }



    private fun NotificationManager.buildChannel(channelId : String = NOTIFICATION_CHANNEL_ID) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Block Foreground Service"
            val descriptionText = "Essential channel for managing the blocking service"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            createNotificationChannel(channel)
        }
    }
}