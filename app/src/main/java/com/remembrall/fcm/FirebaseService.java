package com.remembrall.fcm;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.remembrall.api.Backend;
import com.remembrall.locator.ServiceLocator;

public class FirebaseService extends FirebaseMessagingService {
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
        Log.d("TAG", "RECEIVED!");
    }
}
