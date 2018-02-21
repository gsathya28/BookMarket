package com.clairvoyance.bookmarket;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import java.util.Calendar;

/**
 * Created by Sathya on 1/31/2018.
 * Notification Publisher Class - Handles Notification Requests
 */

class NotificationPublisher extends BroadcastReceiver {

    public static int NOTIFICATION_ID = 2899;
    public static String NOTIFICATION_STRING_ID = "notification_string";
    public static String NAME = "name";
    public static String NOTES = "notes";

    public final static int START = 0;

    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = getNotification(context, intent);
        int id = intent.getIntExtra(NOTIFICATION_STRING_ID, 0);
        notificationManager.notify(id, notification);
    }

    Notification getNotification(Context context, Intent intent) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(intent.getStringExtra(NAME));
        builder.setContentText(intent.getStringExtra(NOTES));
        builder.setSmallIcon(R.drawable.ic_notifications_black_24dp);
        builder.setVibrate(new long[] {1000, });
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(sound);

        // For leading user into main Activity
        Intent resultIntent = new Intent(context, ActSellMainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);

        return builder.build();
    }

    static void scheduleNotification(Context context, PendingIntent pendingIntent) {
        long futureInMillis = Calendar.getInstance().getTimeInMillis();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);
        }
    }
}
