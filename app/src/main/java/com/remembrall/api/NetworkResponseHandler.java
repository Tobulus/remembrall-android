package com.remembrall.api;

import com.remembrall.api.backend.Backend;
import com.remembrall.listener.LoginRequiredListener;
import com.remembrall.locator.ServiceLocator;

import java.util.ArrayList;
import java.util.List;

public class NetworkResponseHandler implements LoginRequiredListener {

    private List<LoginRequiredListener> loginListeners = new ArrayList<>();

    @Override
    public void onLoginRequired() {
        ServiceLocator.getInstance().get(Backend.class).dropLocalDatabase();
        loginListeners.forEach(LoginRequiredListener::onLoginRequired);
    }

    public void register(LoginRequiredListener listener) {
        loginListeners.add(listener);
    }
}
