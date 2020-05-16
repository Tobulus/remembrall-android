package com.groceries.servicelocater;

import android.app.Application;
import com.groceries.api.Backend;
import com.groceries.api.NetworkResponseHandler;

public class ServiceLocationInitializer extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NetworkResponseHandler handler = new NetworkResponseHandler();
        ServiceLocator.getInstance().put(NetworkResponseHandler.class, handler);
        ServiceLocator.getInstance()
                      .put(Backend.class, new Backend(getApplicationContext(), handler));
    }
}
