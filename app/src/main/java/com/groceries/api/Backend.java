package com.groceries.api;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.groceries.model.GroceryList;
import com.groceries.model.GroceryListEntry;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class Backend {

    private final static String BACKEND_FILE = "Backend";
    private final static String TOKEN_KEY = "token";

    private static Backend instance;
    private static final Object singletonLock = new Object();
    private final Context ctx;

    private RequestQueue queue;
    private String url;

    private String token;

    private Backend(Context ctx){
        this.ctx = ctx;
        this.queue  = Volley.newRequestQueue(ctx);
        this.url = "http://192.168.0.249:8080";
    }

    public static void initBackend(Context ctx){
        if (instance == null) {
            synchronized (singletonLock) {
                if (instance == null) {
                    instance = new Backend(ctx);
                } else {
                    throw new IllegalStateException("Backend was already initialized!");
                }
            }
        } else {
            throw new IllegalStateException("Backend was already initialized!");
        }
    }

    public static Backend get() {
        if (instance == null) {
            throw new IllegalStateException("Backend isn't initialized!");
        }

        return instance;
    }

    public void login(String user, String password, Response.Listener<JSONObject> onSuccess, Response.ErrorListener onError) {
        AtomicReference<ApiLoginRequest> request = new AtomicReference<>();
        request.set(new ApiLoginRequest(url +  "/api/auth", user, password, json -> {
            token = request.get().getToken();
            ctx.getSharedPreferences(BACKEND_FILE, Context.MODE_PRIVATE).edit().putString(TOKEN_KEY, token).apply();
            onSuccess.onResponse(json);
        }, onError));
        queue.add(request.get());
    }

    public void getGroceryLists(Consumer<List<GroceryList>> listConsumer, Response.ErrorListener errorListener) {
        ApiArrayRequest json = new ApiArrayRequest(url +  "/api/grocery-lists", response -> {
            ObjectMapper mapper = new ObjectMapper();
            try {
                listConsumer.accept(mapper.readValue(response.toString(), new TypeReference<List<GroceryList>>(){}));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, errorListener, token);

        queue.add(json);
    }

    public void getGroceryListEntries(Long groceryList, Consumer<List<GroceryListEntry>> listConsumer, Response.ErrorListener errorListener) {
        String requestUrl = url +  "/api/grocery-list/" + groceryList + "/entries";

        ApiArrayRequest json = new ApiArrayRequest(requestUrl, response -> {
            ObjectMapper mapper = new ObjectMapper();
            try {
                listConsumer.accept(mapper.readValue(response.toString(), new TypeReference<List<GroceryListEntry>>(){}));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, errorListener, token);

        queue.add(json);
    }
}
