package com.groceries.api;

import android.content.Context;
import android.content.Intent;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.groceries.model.GroceryList;
import com.groceries.model.GroceryListEntry;
import com.groceries.ui.LoginActivity;

import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class Backend {

    private final static String BACKEND_PREFS = "Backend";
    private final static String TOKEN_KEY = "token";

    private final Context ctx;

    private RequestQueue queue;
    private String url;

    private String token;

    public Backend(Context ctx) {
        this.ctx = ctx;
        this.queue  = Volley.newRequestQueue(ctx);
        this.url = "http://192.168.0.248:8080";
    }

    public boolean restoreSession() {
        token = ctx.getSharedPreferences(BACKEND_PREFS, Context.MODE_PRIVATE).getString(TOKEN_KEY, null);
        return token != null;
    }

    private void initToken(){
        if (token == null) {
            synchronized (this) {
                if (token == null) {
                    restoreSession();
                }
            }
        }
    }

    public void login(String user, String password, Response.Listener<JSONObject> onSuccess, Response.ErrorListener onError) {
        AtomicReference<ApiLoginRequest> request = new AtomicReference<>();
        request.set(new ApiLoginRequest(url +  "/api/auth", user, password, json -> {
            token = request.get().getToken();
            ctx.getSharedPreferences(BACKEND_PREFS, Context.MODE_PRIVATE).edit().putString(TOKEN_KEY, token).apply();
            onSuccess.onResponse(json);
        }, onError));
        queue.add(request.get());
    }

    public void createGroceryList(GroceryList groceryList, Consumer<String> onSuccess, Response.ErrorListener errorListener){
        initToken();

        Map<String, String> postParams = new HashMap<>();
        postParams.put("name", groceryList.getName());

        ApiPostRequest request = new ApiPostRequest(url + "/api/grocery-list/new", onSuccess::accept, error -> onErrorHandler(error, errorListener), token, postParams);
        queue.add(request);
    }

    public void createGroceryListEntry(Long groceryListId, GroceryListEntry entry, Consumer<String> onSuccess, Response.ErrorListener errorListener){
        initToken();

        Map<String, String> postParams = new HashMap<>();
        postParams.put("name", entry.getName());
        String requestUrl = url + "/api/grocery-list/" + groceryListId + "/entry/new";

        ApiPostRequest request = new ApiPostRequest(requestUrl, onSuccess::accept, error -> onErrorHandler(error, errorListener), token, postParams);
        queue.add(request);
    }

    public void getGroceryLists(Consumer<List<GroceryList>> listConsumer, Response.ErrorListener errorListener) {
        initToken();

        ApiArrayRequest json = new ApiArrayRequest(url +  "/api/grocery-lists", response -> {
            ObjectMapper mapper = new ObjectMapper();
            try {
                listConsumer.accept(mapper.readValue(response.toString(), new TypeReference<List<GroceryList>>(){}));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, error -> onErrorHandler(error, errorListener), token);

        queue.add(json);
    }

    public void getGroceryListEntries(Long groceryList, Consumer<List<GroceryListEntry>> listConsumer, Response.ErrorListener errorListener) {
        initToken();
        String requestUrl = url +  "/api/grocery-list/" + groceryList + "/entries";

        ApiArrayRequest json = new ApiArrayRequest(requestUrl, response -> {
            ObjectMapper mapper = new ObjectMapper();
            try {
                listConsumer.accept(mapper.readValue(response.toString(), new TypeReference<List<GroceryListEntry>>(){}));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, error -> onErrorHandler(error, errorListener), token);

        queue.add(json);
    }

    private void onErrorHandler(VolleyError error, Response.ErrorListener customErrorListener) {
        int code = error.networkResponse.statusCode;

        if (code == HttpURLConnection.HTTP_FORBIDDEN || code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            // session invalid or expired
            // TODO: this is not working
            Intent showLogin = new Intent(ctx, LoginActivity.class);
            ctx.startActivity(showLogin);
            return;
        }

        customErrorListener.onErrorResponse(error);
    }
}
