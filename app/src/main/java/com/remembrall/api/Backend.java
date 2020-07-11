package com.remembrall.api;

import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.remembrall.BuildConfig;
import com.remembrall.api.data.GroceryListData;
import com.remembrall.api.data.GroceryListEntryData;
import com.remembrall.api.data.InvitationData;
import com.remembrall.api.request.ApiArrayRequest;
import com.remembrall.api.request.ApiDeleteRequest;
import com.remembrall.api.request.ApiLoginRequest;
import com.remembrall.api.request.ApiPostRequest;
import com.remembrall.api.request.ApiPutRequest;
import com.remembrall.fcm.FirebaseService;
import com.remembrall.listener.LoginRequiredListener;
import com.remembrall.model.database.GroceryList;
import com.remembrall.model.database.GroceryListEntry;
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
    private final LoginRequiredListener loginRequiredListener;

    private RequestQueue queue;
    private final String url = BuildConfig.SERVER_URL;

    private String token;

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

    public void logout(Response.Listener<String> onSuccess, Response.ErrorListener onError) {
        token = null;
        ctx.getSharedPreferences(BACKEND_PREFS, Context.MODE_PRIVATE)
           .edit()
           .remove(TOKEN_KEY)
           .apply();

        String requestUrl = url + "/api/logout";
        ApiPostRequest request = new ApiPostRequest(requestUrl, s -> {
            loginRequiredListener.onLoginRequired();
            onSuccess.onResponse(s);
        }, onError, token, null);
        queue.add(request);
    }

    public void register(String user,
                         String firstname,
                         String lastname,
                         String password,
                         String matchingPassword,
                         Consumer<String> onSuccess,
                         Response.ErrorListener errorListener) {
        String requestUrl = url + "/api/user/registration";

        Map<String, String> params = new HashMap<>();
        params.put("username", user);
        params.put("firstname", firstname);
        params.put("lastname", lastname);
        params.put("password", password);
        params.put("matchingPassword", matchingPassword);

        ApiPostRequest request = new ApiPostRequest(requestUrl,
                                                    onSuccess::accept,
                                                    error -> onErrorHandler(error, errorListener),
                                                    token,
                                                    params);
        queue.add(request);
    }

    public void createInvitation(Long groceryListId,
                                 String email,
                                 Consumer<String> onSuccess,
                                 Response.ErrorListener errorListener) {
        Map<String, String> postParams = new HashMap<>();
        postParams.put("email", email);

        ApiPostRequest request =
                new ApiPostRequest(url + "/api/grocery-list/" + groceryListId + "/invite",
                                   onSuccess::accept,
                                   error -> onErrorHandler(error, errorListener),
                                   token,
                                   postParams);
        queue.add(request);
    }

    public void createGroceryList(GroceryListData groceryList,
                                  Consumer<String> onSuccess,
                                  Response.ErrorListener errorListener) {
        Map<String, String> postParams = new HashMap<>();
        postParams.put("name", groceryList.getName());

        ApiPostRequest request = new ApiPostRequest(url + "/api/grocery-list/new",
                                                    onSuccess::accept,
                                                    error -> onErrorHandler(error, errorListener),
                                                    token,
                                                    postParams);
        queue.add(request);
    }

    public void updateGroceryList(GroceryList list,
                                  Response.Listener<String> onSuccess,
                                  Response.ErrorListener errorListener) {
        String requestUrl = url + "/api/grocery-list/" + list.getId();
        ApiPostRequest request = new ApiPostRequest(requestUrl,
                                                    onSuccess,
                                                    errorListener,
                                                    token,
                                                    list.toData().toMap());
        queue.add(request);
    }

    public void deleteGroceryList(GroceryList list,
                                  Response.Listener<String> onSuccess,
                                  Response.ErrorListener errorListener) {
        String requestUrl = url + "/api/grocery-list/" + list.getId() + "/delete";
        ApiDeleteRequest request =
                new ApiDeleteRequest(requestUrl, onSuccess, errorListener, token);
        queue.add(request);
    }

    public void deleteGroceryListEntry(Long listId,
                                       GroceryListEntry entry,
                                       Response.Listener<String> onSuccess,
                                       Response.ErrorListener errorListener) {
        String requestUrl =
                url + "/api/grocery-list/" + listId + "/entry/" + entry.getId() + "/delete";
        ApiDeleteRequest request =
                new ApiDeleteRequest(requestUrl, onSuccess, errorListener, token);
        queue.add(request);
    }

    public void createGroceryListEntry(Long groceryListId,
                                       GroceryListEntryData entry,
                                       Consumer<String> onSuccess,
                                       Response.ErrorListener errorListener) {
        String requestUrl = url + "/api/grocery-list/" + groceryListId + "/entry/new";
        ApiPostRequest request = new ApiPostRequest(requestUrl,
                                                    onSuccess::accept,
                                                    error -> onErrorHandler(error, errorListener),
                                                    token,
                                                    entry.toMap());
        queue.add(request);
    }

    public void getInvitations(Consumer<List<InvitationData>> listConsumer,
                               Response.ErrorListener errorListener) {
        ApiArrayRequest json = new ApiArrayRequest(url + "/api/invitations", response -> {
            ObjectMapper mapper = new ObjectMapper();
            try {
                listConsumer.accept(mapper.readValue(response.toString(),
                                                     new TypeReference<List<InvitationData>>() {
                                                     }));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, error -> onErrorHandler(error, errorListener), token);

        queue.add(json);
    }

    public void getGroceryLists(boolean archived,
                                Consumer<List<GroceryListData>> listConsumer,
                                Response.ErrorListener errorListener) {
        String api = archived ? "/api/archived-grocery-lists" : "/api/grocery-lists";

        ApiArrayRequest json = new ApiArrayRequest(url + api, response -> {
            ObjectMapper mapper = new ObjectMapper();
            try {
                listConsumer.accept(mapper.readValue(response.toString(),
                                                     new TypeReference<List<GroceryListData>>() {
                                                     }));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, error -> onErrorHandler(error, errorListener), token);

        queue.add(json);
    }

    public void getGroceryListEntries(Long groceryList,
                                      Consumer<List<GroceryListEntryData>> listConsumer,
                                      Response.ErrorListener errorListener) {
        String requestUrl = url + "/api/grocery-list/" + groceryList + "/entries";

        ApiArrayRequest json = new ApiArrayRequest(requestUrl, response -> {
            ObjectMapper mapper = new ObjectMapper();
            try {
                listConsumer.accept(mapper.readValue(response.toString(),
                                                     new TypeReference<List<GroceryListEntryData>>() {
                                                     }));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, error -> onErrorHandler(error, errorListener), token);

        queue.add(json);
    }

    private void onErrorHandler(VolleyError error, Response.ErrorListener customErrorListener) {
        if (error.networkResponse != null) {
            int code = error.networkResponse.statusCode;

            if (code == HttpURLConnection.HTTP_FORBIDDEN
                || code == HttpURLConnection.HTTP_UNAUTHORIZED) {
                loginRequiredListener.onLoginRequired();
                return;
            }
        }

        customErrorListener.onErrorResponse(error);
    }

    public void updateGroceryListEntry(Long groceryListId,
                                       GroceryListEntry entry,
                                       Response.Listener<String> onSuccess,
                                       Response.ErrorListener errorListener) {
        String requestUrl = url + "/api/grocery-list/" + groceryListId + "/entry/" + entry.getId();
        ApiPostRequest request = new ApiPostRequest(requestUrl,
                                                    onSuccess,
                                                    errorListener,
                                                    token,
                                                    entry.toData().toMap());
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

    public void registerFirebaseToken(Response.Listener<String> onSuccess,
                                      Response.ErrorListener errorListener) {
        String requestUrl = url + "/api/user/token";
        Map<String, String> params = new HashMap<>();
        params.put("token", FirebaseService.getToken(ctx));
        ApiPutRequest request =
                new ApiPutRequest(requestUrl, onSuccess, errorListener, token, params);
        queue.add(request);
    }

    public void resetPassword(String email,
                              Response.Listener<String> onSuccess,
                              Response.ErrorListener errorListener) {
        String requestUrl = url + "/api/user/reset-password";
        Map<String, String> params = new HashMap<>();
        params.put("username", email);
        ApiPutRequest request =
                new ApiPutRequest(requestUrl, onSuccess, errorListener, token, params);
        queue.add(request);
    }

    public void changePassword(String oldPassword,
                               String newPassword,
                               Response.Listener<String> onSuccess,
                               Response.ErrorListener errorListener) {
        String requestUrl = url + "/api/user/change-password";
        Map<String, String> params = new HashMap<>();
        params.put("oldPassword", oldPassword);
        params.put("newPassword", newPassword);
        ApiPutRequest request =
                new ApiPutRequest(requestUrl, onSuccess, errorListener, token, params);
        queue.add(request);
    }
}
