package cat.xojan.random1.ui.controller;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

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
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctx)
                        .setSmallIcon(R.drawable.ic_podcast_notification)
                        .setContentTitle(ctx.getString(R.string.app_name))
                        .setContentText(podcast.category() + " " + podcast.description());
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(ctx, cls);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(cls);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(notificationId, mBuilder.build());
    }

    public void dissmissNotification(int notificationId) {
        mNotificationManager.cancel(notificationId);
    }

    public void destroy() {
        mNotificationManager = null;
    }
}
