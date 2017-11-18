package cat.xojan.random1.ui.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.app.NotificationCompat.MediaStyle
import android.support.v4.media.session.MediaButtonReceiver
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import cat.xojan.random1.R
import cat.xojan.random1.service.MediaPlaybackService


class NotificationController(private val service: MediaPlaybackService,
                             private val mediaSession: MediaSessionCompat) {

    private val notificationId = "podcastsRAC1"
    private val notificationName = service.getString(R.string.app_name)

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(
                notificationId,
                notificationName,
                importance)

        val notificationManager = service.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun showPlaying() {
        val builder = createNotificationBuilder(
                android.R.drawable.ic_media_pause,
                "Pause",
                PlaybackStateCompat.ACTION_PLAY_PAUSE)
        notify(builder)
    }

    fun showPaused() {
        val builder = createNotificationBuilder(
                android.R.drawable.ic_media_play,
                "Play",
                PlaybackStateCompat.ACTION_PLAY_PAUSE)
        notify(builder)
    }

    private fun notify(builder: NotificationCompat.Builder) {
        NotificationManagerCompat.from(service).notify(1, builder.build())
    }

    private fun createNotificationBuilder(drawable: Int,
                                          title: String,
                                          playBackState: Long
    ): NotificationCompat.Builder {
        val builder = from(service, mediaSession)
        builder.addAction(NotificationCompat.Action(
                drawable,
                title,
                MediaButtonReceiver.buildMediaButtonPendingIntent(service, playBackState)))
        builder.setStyle(
                MediaStyle()
                        .setShowActionsInCompactView(0)
                        .setMediaSession(mediaSession.sessionToken))
        builder.setSmallIcon(R.mipmap.ic_notification)
        return builder
    }

    /**
     * Build a notification using the information from the given media session. Makes heavy use
     * of [MediaMetadataCompat.getDescription] to extract the appropriate information.
     * @param context Context used to construct the notification.
     * @param mediaSession Media session to get information.
     * @return A pre-built notification with information from the given media session.
     */
    private fun from(
            context: Context, mediaSession: MediaSessionCompat): NotificationCompat.Builder {
        val controller = mediaSession.controller
        val mediaMetadata = controller.metadata
        val description = mediaMetadata.description

        val builder = NotificationCompat.Builder(context, notificationId)
        builder
                .setContentTitle(description.title)
                .setContentText(description.subtitle)
                .setSubText(description.description)
                .setLargeIcon(description.iconBitmap)
                .setContentIntent(controller.sessionActivity)
                .setDeleteIntent(MediaButtonReceiver
                        .buildMediaButtonPendingIntent(context, PlaybackStateCompat.ACTION_STOP))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        return builder
    }

    fun release() {
        NotificationManagerCompat.from(service).cancel(1)
    }
}