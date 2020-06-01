package com.remembrall.locator;

import android.app.Application;
import androidx.room.Room;
import com.remembrall.api.Backend;
import com.remembrall.api.NetworkResponseHandler;
import com.remembrall.model.database.Database;

public class ServiceLocatorInitializer extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NetworkResponseHandler handler = new NetworkResponseHandler();
        ServiceLocator.getInstance().put(NetworkResponseHandler.class, handler);
        ServiceLocator.getInstance()
                      .put(Backend.class, new Backend(getApplicationContext(), handler));
        ServiceLocator.getInstance().put(Database.class, Room.databaseBuilder(getApplicationContext(),
                                                                              Database.class,
                                                                              "remembrall")
                                                             .allowMainThreadQueries()
                                                             .build());
    }
}
