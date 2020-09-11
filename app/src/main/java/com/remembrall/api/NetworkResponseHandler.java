package com.remembrall.api;

import com.remembrall.api.backend.Backend;
import com.remembrall.listener.FailedListener;
import com.remembrall.listener.LoginRequiredListener;
import com.remembrall.locator.ServiceLocator;

import java.util.ArrayList;
import java.util.List;

public class NetworkResponseHandler implements LoginRequiredListener, FailedListener {

    private List<LoginRequiredListener> loginListeners = new ArrayList<>();
    private List<FailedListener> failListeners = new ArrayList<>();

    @Override
    public void onLoginRequired() {
        ServiceLocator.getInstance().get(Backend.class).dropLocalDatabase();
        loginListeners.forEach(LoginRequiredListener::onLoginRequired);
    }

    @Override
    public void onFail(String message) {
        failListeners.forEach(listener -> listener.onFail(message));
    }

    public void register(LoginRequiredListener listener) {
        loginListeners.add(listener);
    }

    public void register(FailedListener listener) {
        failListeners.add(listener);
    }
}
