package com.driving.groceries.ui.login;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class Backend {

    private static Backend instance;
    private static final Object singletonLock = new Object();

    private RequestQueue queue;
    private String url;

    private Backend(Context ctx){
        this.queue  = Volley.newRequestQueue(ctx);
        this.url = "http://192.168.0.249:8080";
    }

    public static void initBackend(Context ctx){
        if (instance == null) {
            synchronized (singletonLock) {
                if (instance == null) {
                    instance = new Backend( ctx);
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

    public void getGroceryLists(Consumer<List<GroceryList>> listConsumer, Response.ErrorListener errorListener) {
        JsonArrayRequest json = new JsonArrayRequest(Request.Method.GET, url +  "/service/json/grocery-lists", null, response -> {
            ObjectMapper mapper = new ObjectMapper();
            try {
                listConsumer.accept(mapper.readValue(response.toString(), new TypeReference<List<GroceryList>>(){}));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, errorListener);

        queue.add(json);
    }
}
