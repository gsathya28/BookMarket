package com.clairvoyance.bookmarket;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by Sathya on 1/24/2018.
 *
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final static String TAG = "MyAndroidFCMService";
    private final static String CHANNEL_ID = "channelID";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        createNotification(remoteMessage);
    }

    private void createNotification(RemoteMessage remoteMessage) {

        try {
            User mainUser = WebServiceHandler.generateMainUser();
        }catch (IllegalAccessException i){
            Log.d("NotifAccess", i.getMessage());
        }
        RemoteMessage.Notification notification = remoteMessage.getNotification();

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification());

        if(notification == null){
            return;
        }

        Map<String, String> data = remoteMessage.getData();

        Intent intent = new Intent( this , ActSellViewBooks.class );

        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);

        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("bookID", data.get("bookID"));
        PendingIntent resultIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager notificationManager = (NotificationManager) (this.getSystemService(Context.NOTIFICATION_SERVICE));

        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder( this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setAutoCancel( true )
                .setSound(notificationSoundURI)
                .setContentIntent(resultIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            CharSequence name = "Example Channel Name";
            String description = "Example Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        if (notificationManager != null) {
            notificationManager.notify(0, mNotificationBuilder.build());
        }
    }
}
