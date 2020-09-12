package com.remembrall.api.backend;

import android.content.Context;
import com.android.volley.Response;
import com.remembrall.R;
import com.remembrall.api.request.ApiLoginRequest;
import com.remembrall.api.request.ApiPostRequest;
import com.remembrall.api.request.ApiPutRequest;
import com.remembrall.locator.ServiceLocator;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class UserBackend {

    private final Backend backend;

    public UserBackend(Backend backend) {
        this.backend = backend;
    }

    public void login(String user,
                      String password,
                      Locale locale,
                      Response.Listener<JSONObject> onSuccess,
                      Response.ErrorListener customErrorListener) {
        AtomicReference<ApiLoginRequest> request = new AtomicReference<>();
        request.set(new ApiLoginRequest(backend.url + "/api/auth", user, password, locale, json -> {
            backend.token = request.get().getToken();
            backend.ctx.getSharedPreferences(Backend.BACKEND_PREFS, Context.MODE_PRIVATE)
                       .edit()
                       .putString(Backend.TOKEN_KEY, backend.token)
                       .apply();
            onSuccess.onResponse(json);
        }, error -> backend.onErrorHandler(error, customErrorListener, R.string.login_failed)));
        backend.queue.add(request.get());
    }

    public void logout() {
        backend.ctx.getSharedPreferences(Backend.BACKEND_PREFS, Context.MODE_PRIVATE)
                   .edit()
                   .remove(Backend.TOKEN_KEY)
                   .apply();

        String requestUrl = backend.url + "/api/logout";
        ApiPostRequest request = new ApiPostRequest(requestUrl, s -> {
            backend.token = null;
            ServiceLocator.getInstance().get(Backend.class).dropLocalDatabase();
            backend.loginRequiredListener.onLoginRequired();
        }, null, backend.token, null);
        backend.queue.add(request);
    }

    public void register(String user,
                         String firstname,
                         String lastname,
                         String password,
                         String matchingPassword,
                         Consumer<String> onSuccess,
                         Response.ErrorListener errorListener) {
        String requestUrl = backend.url + "/api/user/registration";

        Map<String, String> params = new HashMap<>();
        params.put("username", user);
        params.put("firstname", firstname);
        params.put("lastname", lastname);
        params.put("password", password);
        params.put("matchingPassword", matchingPassword);

        ApiPostRequest request = new ApiPostRequest(requestUrl,
                                                    onSuccess::accept,
                                                    error -> backend.onErrorHandler(error,
                                                                                    errorListener,
                                                                                    R.string.loading_failed),
                                                    backend.token,
                                                    params);
        backend.queue.add(request);
    }

    public void resetPassword(String email,
                              Response.Listener<String> onSuccess,
                              Response.ErrorListener errorListener) {
        String requestUrl = backend.url + "/api/user/reset-password";
        Map<String, String> params = new HashMap<>();
        params.put("username", email);
        ApiPutRequest request =
                new ApiPutRequest(requestUrl, onSuccess, errorListener, backend.token, params);
        backend.queue.add(request);
    }

    public void changePassword(String oldPassword, String newPassword) {
        String requestUrl = backend.url + "/api/user/change-password";
        Map<String, String> params = new HashMap<>();
        params.put("oldPassword", oldPassword);
        params.put("newPassword", newPassword);
        ApiPutRequest request = new ApiPutRequest(requestUrl, s -> {
        }, null, backend.token, params);
        backend.queue.add(request);
    }
}
