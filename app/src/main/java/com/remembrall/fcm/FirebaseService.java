package com.remembrall.fcm;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.remembrall.R;
import com.remembrall.api.backend.Backend;
import com.remembrall.locator.ServiceLocator;
import com.remembrall.ui.MainActivity;

import java.util.concurrent.atomic.AtomicInteger;

import static com.remembrall.locator.ServiceLocatorInitializer.CHANNEL_DEFAULT;

public class FirebaseService extends FirebaseMessagingService {

    private final static AtomicInteger notificationId = new AtomicInteger(0);

    public static String getToken(Context context) {
        return context.getSharedPreferences("_", MODE_PRIVATE).getString("fb", "empty");
    }

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d("TAG", "Refreshed token: " + token);
        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fb", token).apply();

        if (ServiceLocator.getInstance().get(Backend.class).isSessionAvailable()) {
            // TODO: if this fails we need to retry
            ServiceLocator.getInstance().get(Backend.class).registerFirebaseToken(s -> {
            }, e -> {
            });
        }
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            Intent intent =
                    new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                                                        .putExtra(MainActivity.INTENT_NAME,
                                                                  remoteMessage.getNotification().getClickAction());

            if (remoteMessage.getData().get("listId") != null) {
                intent.putExtra("listId", remoteMessage.getData().get("listId"));
            }

            PendingIntent pendingIntent =
                    PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(this, CHANNEL_DEFAULT).setContentTitle(title)
                                                                         .setPriority(
                                                                                 NotificationCompat.PRIORITY_DEFAULT)
                                                                         .setContentText(body)
                                                                         .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                                                                         .setContentIntent(
                                                                                 pendingIntent)
                                                                         .setAutoCancel(true);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(notificationId.incrementAndGet(), builder.build());
        } else {
            Log.d("firebase", "getNotification() was null");
        }
    }
}
