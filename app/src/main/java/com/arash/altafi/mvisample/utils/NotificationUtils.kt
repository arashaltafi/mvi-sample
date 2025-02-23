package com.arash.altafi.mvisample.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.arash.altafi.mvisample.R
import com.arash.altafi.mvisample.MainActivity

object NotificationUtils {

    const val NOTIFICATION_ID = "CANCEL_NOTIF_ID"
    const val CHANNEL_ID = "ChatAndroid_Notification_channel"

    fun sendNotification(
        context: Context,
        title: String,
        message: String,
        notifId: Int = (0..100000000).random()
    ) {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 1, intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create notification channel for Android O and above.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                context.getString(R.string.public_),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = context.getString(R.string.public_app_notifications)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.icon)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setOnlyAlertOnce(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        // For Android TIRAMISU and above, check that POST_NOTIFICATIONS permission is granted.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                    PermissionUtils.isPermissionGranted(context, Manifest.permission.POST_NOTIFICATIONS))) {
            notificationManager.notify(notifId, builder.build())
        }
    }

    @SuppressLint("LaunchActivityFromNotification", "UnspecifiedImmutableFlag")
    fun sendNotificationByIntent(
        context: Context,
        channelId: String,
        title: String,
        body: String,
        bodyBig: String? = null,
        image: Bitmap? = null,
        bigImage: Bitmap? = null,
        notifId: Int = (0..1000000).random(),
        intent: Intent? = null,
        button: String? = null,
        btnIntent: Intent? = null,
    ) {
        val pendingIntent: PendingIntent? = intent?.let {
            it.putExtra(NOTIFICATION_ID, notifId)

            it.flags =
                (intent.flags or Intent.FLAG_ACTIVITY_CLEAR_TASK)

            PendingIntent.getActivity(
                context, 0, intent,
                (PendingIntent.FLAG_IMMUTABLE)
                        or
                        PendingIntent.FLAG_UPDATE_CURRENT
            )

        } ?: run {
            PendingIntent.getActivity(
                context, 0, Intent().apply { putExtra(NOTIFICATION_ID, notifId) },
                (PendingIntent.FLAG_IMMUTABLE)
                        or
                        PendingIntent.FLAG_CANCEL_CURRENT
            )
        }

        val btnPendingIntent: PendingIntent? = btnIntent?.let {
            it.putExtra(NOTIFICATION_ID, notifId)

            it.flags =
                (it.flags or Intent.FLAG_ACTIVITY_CLEAR_TASK)

            PendingIntent.getActivity(
                context, 0, btnIntent,
                (PendingIntent.FLAG_IMMUTABLE)
                        or
                        PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        send(
            context,
            channelId,
            title,
            body,
            bodyBig,
            image,
            bigImage,
            notifId,
            pendingIntent,
            button,
            btnPendingIntent
        )
    }

    private fun send(
        context: Context,
        channelId: String,
        title: String,
        body: String,
        bodyBig: String? = null,
        image: Bitmap? = null,
        bigImage: Bitmap? = null,
        notifId: Int = (0..1000000).random(),
        pendingIntent: PendingIntent? = null,
        button: String? = null,
        btnPendingIntent: PendingIntent? = null,
    ) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setChannelId(channelId)

            .setSmallIcon(R.drawable.icon)
            .setLargeIcon(image)

            .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)

            .setContentTitle(Utils.setPersianDigits(title))
            .setContentText(Utils.setPersianDigits(body))

            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSound(defaultSoundUri)// api.version < android.O

        if (bigImage != null) {
            notificationBuilder.setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(bigImage)
            )
        } else {
            notificationBuilder.setStyle(
                NotificationCompat.BigTextStyle().bigText(bodyBig.takeIf { it.isNullOrEmpty() })
            )
        }

        button?.let {
            notificationBuilder
                .addAction(
                    R.drawable.shp_transparent,
                    button,
                    btnPendingIntent
                )
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            notificationBuilder.setContentIntent(pendingIntent)
            notificationManager.notify(notifId, notificationBuilder.build())
        } else if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            PermissionUtils.isPermissionGranted(context, Manifest.permission.POST_NOTIFICATIONS)
        ) {
            notificationBuilder.setContentIntent(pendingIntent)
            notificationManager.notify(notifId, notificationBuilder.build())
        }
    }

    /**
     * create channel if is not created
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun createChannel(
        context: Context,
        channelId: String,
        name: String,
        descriptionText: String = "",
        importance: Int = NotificationManager.IMPORTANCE_DEFAULT,
        visibility: Int = Notification.VISIBILITY_PUBLIC,
        groupId: String? = null,
        soundUri: Uri? = null,
        showBadge: Boolean = true,
    ) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val isChannelCreated = try {
            notificationManager.getNotificationChannel(channelId)
            true
        } catch (_: Exception) {
            false
        }

        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_ALARM)
            .build()
        val soundURI = soundUri ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        if (isChannelCreated.not()) {
            val mChannel = NotificationChannel(channelId, name, importance).apply {
                groupId?.let {
                    group = it
                }
                description = descriptionText
                lockscreenVisibility = visibility
                when (importance) {
                    NotificationManager.IMPORTANCE_HIGH,
                    NotificationManager.IMPORTANCE_DEFAULT -> {
                        setSound(soundURI, audioAttributes)
                    }

                    NotificationManager.IMPORTANCE_LOW -> {
                        if (soundUri != null)
                            setSound(soundURI, audioAttributes)
                    }
                }
                setShowBadge(showBadge)
            }
            notificationManager.createNotificationChannel(mChannel)
        }

    }
}