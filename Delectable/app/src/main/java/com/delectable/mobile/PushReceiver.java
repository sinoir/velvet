package com.delectable.mobile;

import com.delectable.mobile.util.DeepLink;
import com.kahuna.sdk.KahunaAnalytics;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class PushReceiver extends BroadcastReceiver {

    private static final String TAG = PushReceiver.class.getSimpleName();

    public static final String URL_KEY = "url";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }
        String action = intent.getAction();
        if (action == null) {
            return;
        }
        //Deep-linking parameters.
        Bundle kahunaExtras = intent.getBundleExtra(KahunaAnalytics.EXTRA_LANDING_DICTIONARY_ID);
        String deepLink = kahunaExtras.getString(URL_KEY);
        Log.d(TAG, "Kahuna URL: " + deepLink);
        if (deepLink == null) {
            return;
        }

        if (action.equals(KahunaAnalytics.ACTION_PUSH_RECEIVED)) {
            //Push notification was received.

            String message = intent.getStringExtra(KahunaAnalytics.EXTRA_PUSH_MESSAGE);
            Log.d(TAG, "Kahuna Message: " + message);
            if (message == null || message.isEmpty()) {
                //if no message, don't display notification
                return;
            }

            Uri data = Uri.parse(deepLink);
            DeepLink deepLinkItem = DeepLink.valueOf(data);
            if (DeepLink.UNKNOWN == deepLinkItem) {
                //not equipped to handle this deeplink, don't allow push notification to reach user
                return;
            }

            //funnel the deeplink through MainActivity, which will handle the rest
            Intent mainActivityIntent = new Intent(context, MainActivity.class);
            mainActivityIntent.setAction(Intent.ACTION_VIEW);
            mainActivityIntent.setData(data);

            PendingIntent piMainActivity = PendingIntent
                    .getActivity(context, 0, mainActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Bitmap icon = BitmapFactory
                    .decodeResource(context.getResources(), R.drawable.ic_launcher);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setContentIntent(piMainActivity)
                    .setSmallIcon(R.drawable.ic_notification_logo)
                    .setLargeIcon(icon)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS)
                    .setTicker(message)
                    .setContentText(message)
                    .setAutoCancel(true);

            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            //mapped to DeepLink enum's ordinal, so that we only have max one type of each notification displayed.
            int notificationId = deepLinkItem.ordinal();
            notificationManager.notify(notificationId, builder.build());
        }
    }
}