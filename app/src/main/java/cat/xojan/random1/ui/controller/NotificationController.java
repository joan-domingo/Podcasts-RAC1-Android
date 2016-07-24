package cat.xojan.random1.ui.controller;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import javax.inject.Inject;

import cat.xojan.random1.R;
import cat.xojan.random1.domain.entity.Podcast;

public class NotificationController {

    private NotificationManager mNotificationManager;

    @Inject
    public NotificationController(NotificationManager notificationManager) {
        mNotificationManager = notificationManager;
    }

    public void showNotification(Class<?> cls, int notificationId, Context ctx, Podcast podcast) {

        final Intent notificationIntent = new Intent(ctx, cls);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0, notificationIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx)
                        .setSmallIcon(R.drawable.ic_podcast_notification)
                        .setContentTitle(ctx.getString(R.string.app_name))
                        .setContentText(podcast.category() + " " + podcast.description());

        builder.setContentIntent(contentIntent);
        mNotificationManager.notify(notificationId, builder.build());
    }

    public void dissmissNotification(int notificationId) {
        mNotificationManager.cancel(notificationId);
    }

    public void destroy() {
        mNotificationManager = null;
    }
}
