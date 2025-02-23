package com.arash.altafi.mvisample.service

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.google.firebase.messaging.RemoteMessage
import com.arash.altafi.mvisample.R
import com.arash.altafi.mvisample.utils.NotificationUtils
import com.google.firebase.messaging.FirebaseMessagingService

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class FirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        Log.i("FCMToken", "onNewToken: $newToken")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.i("FCMToken", "onMessageReceived: $remoteMessage")
        val context = this
        val title = remoteMessage.data["title"] ?: context.getString(R.string.app_name)
        val body = remoteMessage.data["body"] ?: context.getString(R.string.app_name)
        val imageUrl = remoteMessage.data["imageUrl"]
        val url = remoteMessage.data["url"]

        // Handle notification display
        sendNotification(context, title, body, imageUrl, url)
    }

    @SuppressLint("MissingPermission")
    private fun sendNotification(
        context: Context,
        title: String,
        body: String,
        imageUrl: String?,
        url: String?
    ) {
        NotificationUtils.sendNotification(
            context,
            title,
            body,
        )
    }
}