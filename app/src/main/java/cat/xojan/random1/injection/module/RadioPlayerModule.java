package cat.xojan.random1.injection.module;

import android.app.NotificationManager;
import android.content.Context;

import cat.xojan.random1.ui.controller.NotificationController;
import dagger.Module;
import dagger.Provides;

@Module
public class RadioPlayerModule {

    @Provides
    NotificationManager providesNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Provides
    NotificationController providesNotificationController(NotificationManager notificationManager) {
        return new NotificationController(notificationManager);
    }
}
