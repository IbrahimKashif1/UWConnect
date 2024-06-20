package com.uwconnect.android.util

import androidx.core.app.NotificationManagerCompat

object NotificationManager {
    private var askNotificationPermission: () -> Unit = {}

    fun init(askNotificationPermission: () -> Unit) {
        this.askNotificationPermission = askNotificationPermission
    }

    fun requestPermission() {
        askNotificationPermission()
    }
}