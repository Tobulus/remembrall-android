package com.remembrall.locator;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import androidx.room.Room;
import com.remembrall.R;
import com.remembrall.api.NetworkResponseHandler;
import com.remembrall.api.backend.Backend;
import com.remembrall.api.backend.GroceryListBackend;
import com.remembrall.api.backend.GroceryListEntryBackend;
import com.remembrall.api.backend.InvitationBackend;
import com.remembrall.api.backend.UserBackend;
import com.remembrall.model.database.Database;

public class ServiceLocatorInitializer extends Application {

    public static final String CHANNEL_DEFAULT = "channel-default";

    @Override
    public void onCreate() {
        super.onCreate();

        NetworkResponseHandler handler = new NetworkResponseHandler();
        ServiceLocator locator = ServiceLocator.getInstance();
        Backend backend = new Backend(getApplicationContext(), handler);

        locator.put(NetworkResponseHandler.class, handler);
        locator.put(Backend.class, backend);
        locator.put(GroceryListBackend.class, new GroceryListBackend(backend));
        locator.put(GroceryListEntryBackend.class, new GroceryListEntryBackend(backend));
        locator.put(InvitationBackend.class, new InvitationBackend(backend));
        locator.put(UserBackend.class, new UserBackend(backend));
        locator.put(Database.class,
                    Room.databaseBuilder(getApplicationContext(), Database.class, "remembrall")
                        .allowMainThreadQueries()
                        .build());
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
