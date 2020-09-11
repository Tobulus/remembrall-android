package com.remembrall.api.backend;

import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.remembrall.BuildConfig;
import com.remembrall.R;
import com.remembrall.api.NetworkResponseHandler;
import com.remembrall.api.request.ApiPutRequest;
import com.remembrall.fcm.FirebaseService;
import com.remembrall.listener.LoginRequiredListener;
import com.remembrall.locator.ServiceLocator;
import com.remembrall.model.database.Database;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class Backend {

    protected final static String BACKEND_PREFS = "Backend";
    protected final static String TOKEN_KEY = "token";

    protected final Context ctx;
    protected final LoginRequiredListener loginRequiredListener;
    protected final String url = BuildConfig.SERVER_URL;
    protected RequestQueue queue;
    protected String token;

    public Backend(Context ctx, LoginRequiredListener listener) {
        this.ctx = ctx;
        this.queue = Volley.newRequestQueue(ctx);
        this.token = ctx.getSharedPreferences(BACKEND_PREFS, Context.MODE_PRIVATE)
                        .getString(TOKEN_KEY, null);
        this.loginRequiredListener = listener;
    }

    public boolean isSessionAvailable() {
        return token != null;
    }

    public void dropLocalDatabase() {
        ServiceLocator.getInstance().get(Database.class).groceryListRepository().deleteAll(true);
        ServiceLocator.getInstance().get(Database.class).groceryListRepository().deleteAll(false);
        ServiceLocator.getInstance().get(Database.class).groceryListEntryRepository().deleteAll();
    }

    protected void onErrorHandler(VolleyError error, Response.ErrorListener customErrorListener) {
        if (error.networkResponse != null) {
            int code = error.networkResponse.statusCode;

            if (code == HttpURLConnection.HTTP_FORBIDDEN
                || code == HttpURLConnection.HTTP_UNAUTHORIZED) {
                loginRequiredListener.onLoginRequired();
                return;
            }
        }

        ServiceLocator.getInstance()
                      .get(NetworkResponseHandler.class)
                      .onFail(ctx.getString(R.string.loading_failed));

        customErrorListener.onErrorResponse(error);
    }

    public void registerFirebaseToken(Response.Listener<String> onSuccess,
                                      Response.ErrorListener errorListener) {
        String requestUrl = url + "/api/user/token";
        Map<String, String> params = new HashMap<>();
        params.put("token", FirebaseService.getToken(ctx));
        ApiPutRequest request =
                new ApiPutRequest(requestUrl, onSuccess, errorListener, token, params);
        queue.add(request);
    }
}
