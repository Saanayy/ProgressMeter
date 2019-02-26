package com.bytebucket1111.progressmeter.helper;

import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.bytebucket1111.progressmeter.R;


public class NotificationHelper {

    public static void displayNotification(Context context, String title, String body) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "channelId")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, mBuilder.build());
    }
}
