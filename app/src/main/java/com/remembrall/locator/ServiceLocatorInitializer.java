package com.remembrall.locator;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import androidx.room.Room;
import com.remembrall.R;
import com.remembrall.api.Backend;
import com.remembrall.api.NetworkResponseHandler;
import com.remembrall.model.database.Database;

public class ServiceLocatorInitializer extends Application {

    public static final String CHANNEL_DEFAULT = "channel-default";

    @Override
    public void onCreate() {
        super.onCreate();
        NetworkResponseHandler handler = new NetworkResponseHandler();
        ServiceLocator.getInstance().put(NetworkResponseHandler.class, handler);
        ServiceLocator.getInstance()
                      .put(Backend.class, new Backend(getApplicationContext(), handler));
        ServiceLocator.getInstance()
                      .put(Database.class,
                           Room.databaseBuilder(getApplicationContext(),
                                                Database.class,
                                                "remembrall").allowMainThreadQueries().build());
        createDefaultNotificationChannel();
    }

    private void createDefaultNotificationChannel() {
        CharSequence name = getString(R.string.default_channel_name);
        String description = getString(R.string.default_channel_description);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_DEFAULT, name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}
