package com.groceries.api;

import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.groceries.model.GroceryList;
import com.groceries.model.GroceryListEntry;
import com.groceries.model.Invitation;
import com.groceries.ui.activity.LoginRequiredListener;
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
    private final LoginRequiredListener mListener;

    private RequestQueue queue;
    private String url;

    private String token;

    public Backend(Context ctx, LoginRequiredListener listener) {
        this.ctx = ctx;
        this.queue = Volley.newRequestQueue(ctx);
        this.url = "http://192.168.0.248:8080";
        this.mListener = listener;
    }

    public boolean restoreSession() {
        token = ctx.getSharedPreferences(BACKEND_PREFS, Context.MODE_PRIVATE)
                   .getString(TOKEN_KEY, null);
        return token != null;
    }

    private void initToken() {
        if (token == null) {
            synchronized (this) {
                if (token == null) {
                    restoreSession();
                }
            }
        }
    }

    public void login(String user,
                      String password,
                      Response.Listener<JSONObject> onSuccess,
                      Response.ErrorListener onError) {
        AtomicReference<ApiLoginRequest> request = new AtomicReference<>();
        request.set(new ApiLoginRequest(url + "/api/auth", user, password, json -> {
            token = request.get().getToken();
            ctx.getSharedPreferences(BACKEND_PREFS, Context.MODE_PRIVATE)
               .edit()
               .putString(TOKEN_KEY, token)
               .apply();
            onSuccess.onResponse(json);
        }, onError));
        queue.add(request.get());
    }

    public void register(String user,
                         String password,
                         String matchingPassword,
                         Consumer<String> onSuccess,
                         Response.ErrorListener errorListener) {
        String requestUrl = url + "/api/user/registration";

        Map<String, String> params = new HashMap<>();
        params.put("username", user);
        params.put("password", password);
        params.put("matchingPassword", matchingPassword);

        ApiPostRequest request = new ApiPostRequest(requestUrl,
                                                    onSuccess::accept,
                                                    error -> onErrorHandler(error, errorListener),
                                                    token,
                                                    params);
        queue.add(request);
    }

    public void createInvitation(GroceryList groceryList,
                                 String email,
                                 Consumer<String> onSuccess,
                                 Response.ErrorListener errorListener) {
        initToken();

        Map<String, String> postParams = new HashMap<>();
        postParams.put("user", email);

        ApiPostRequest request = new ApiPostRequest(url + "/api/grocery-list/" + groceryList.getId() + "/invite",
                onSuccess::accept,
                error -> onErrorHandler(error, errorListener),
                token,
                postParams);
        queue.add(request);
    }

    public void createGroceryList(GroceryList groceryList,
                                  Consumer<String> onSuccess,
                                  Response.ErrorListener errorListener) {
        initToken();

        Map<String, String> postParams = new HashMap<>();
        postParams.put("name", groceryList.getName());

        ApiPostRequest request = new ApiPostRequest(url + "/api/grocery-list/new",
                                                    onSuccess::accept,
                                                    error -> onErrorHandler(error, errorListener),
                                                    token,
                                                    postParams);
        queue.add(request);
    }

    public void createGroceryListEntry(Long groceryListId,
                                       GroceryListEntry entry,
                                       Consumer<String> onSuccess,
                                       Response.ErrorListener errorListener) {
        initToken();

        Map<String, String> postParams = new HashMap<>();
        postParams.put("name", entry.getName());
        String requestUrl = url + "/api/grocery-list/" + groceryListId + "/entry/new";

        ApiPostRequest request = new ApiPostRequest(requestUrl,
                                                    onSuccess::accept,
                                                    error -> onErrorHandler(error, errorListener),
                                                    token,
                                                    postParams);
        queue.add(request);
    }

    public void getInvitations(Consumer<List<Invitation>> listConsumer,
                               Response.ErrorListener errorListener) {
        initToken();

        ApiArrayRequest json = new ApiArrayRequest(url + "/api/invitations", response -> {
            ObjectMapper mapper = new ObjectMapper();
            try {
                listConsumer.accept(mapper.readValue(response.toString(),
                        new TypeReference<List<Invitation>>() {
                        }));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, error -> onErrorHandler(error, errorListener), token);

        queue.add(json);
    }

    public void getGroceryLists(Consumer<List<GroceryList>> listConsumer,
                                Response.ErrorListener errorListener) {
        initToken();

        ApiArrayRequest json = new ApiArrayRequest(url + "/api/grocery-lists", response -> {
            ObjectMapper mapper = new ObjectMapper();
            try {
                listConsumer.accept(mapper.readValue(response.toString(),
                                                     new TypeReference<List<GroceryList>>() {
                                                     }));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, error -> onErrorHandler(error, errorListener), token);

        queue.add(json);
    }

    public void getGroceryListEntries(Long groceryList,
                                      Consumer<List<GroceryListEntry>> listConsumer,
                                      Response.ErrorListener errorListener) {
        initToken();
        String requestUrl = url + "/api/grocery-list/" + groceryList + "/entries";

        ApiArrayRequest json = new ApiArrayRequest(requestUrl, response -> {
            ObjectMapper mapper = new ObjectMapper();
            try {
                listConsumer.accept(mapper.readValue(response.toString(),
                                                     new TypeReference<List<GroceryListEntry>>() {
                                                     }));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, error -> onErrorHandler(error, errorListener), token);

        queue.add(json);
    }

    private void onErrorHandler(VolleyError error, Response.ErrorListener customErrorListener) {
        int code = error.networkResponse.statusCode;

        if (code == HttpURLConnection.HTTP_FORBIDDEN
            || code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            mListener.onLoginRequired();
            return;
        }

        customErrorListener.onErrorResponse(error);
    }

    public void updateGroceryListEntry(Long groceryListId,
                                       GroceryListEntry entry,
                                       Response.Listener<String> onSuccess,
                                       Response.ErrorListener errorListener) {
        String requestUrl = url + "/api/grocery-list/" + groceryListId + "/entry/" + entry.getId();
        ApiPostRequest request =
                new ApiPostRequest(requestUrl, onSuccess, errorListener, token, entry.toMap());
        queue.add(request);
    }

    public void acknowledge(Long invitationId,
                            Response.Listener<String> onSuccess,
                            Response.ErrorListener errorListener) {
        String requestUrl = url + "/api/invitation/" + invitationId;
        Map<String, String> params = new HashMap<>();
        params.put("ack", "true");
        ApiPostRequest request =
                new ApiPostRequest(requestUrl, onSuccess, errorListener, token, params);
        queue.add(request);
    }

    public void deny(Long invitationId,
                     Response.Listener<String> onSuccess,
                     Response.ErrorListener errorListener) {
        String requestUrl = url + "/api/invitation/" + invitationId;
        Map<String, String> params = new HashMap<>();
        params.put("deny", "true");
        ApiPostRequest request =
                new ApiPostRequest(requestUrl, onSuccess, errorListener, token, params);
        queue.add(request);
    }
}
