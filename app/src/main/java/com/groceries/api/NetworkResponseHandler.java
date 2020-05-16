package com.groceries.api;

import com.groceries.ui.activity.LoginRequiredListener;

import java.util.ArrayList;
import java.util.List;

public class NetworkResponseHandler implements LoginRequiredListener {

    private List<LoginRequiredListener> loginListeners = new ArrayList<>();

    @Override
    public void onLoginRequired() {
        loginListeners.forEach(LoginRequiredListener::onLoginRequired);
    }

    public void register(LoginRequiredListener listener) {
        loginListeners.add(listener);
    }
}
