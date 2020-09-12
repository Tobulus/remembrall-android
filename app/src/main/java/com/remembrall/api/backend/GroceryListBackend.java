package com.remembrall.api.backend;

import com.android.volley.Response;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.remembrall.R;
import com.remembrall.api.data.GroceryListData;
import com.remembrall.api.request.ApiArrayRequest;
import com.remembrall.api.request.ApiDeleteRequest;
import com.remembrall.api.request.ApiPostRequest;
import com.remembrall.model.database.GroceryList;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class GroceryListBackend {

    private final Backend backend;

    public GroceryListBackend(Backend backend) {
        this.backend = backend;
    }

    public void getGroceryLists(boolean archived,
                                Consumer<List<GroceryListData>> listConsumer,
                                Response.ErrorListener errorListener) {
        String api = archived ? "/api/archived-grocery-lists" : "/api/grocery-lists";

        ApiArrayRequest json = new ApiArrayRequest(backend.url + api,
                                                   response -> {
                                                       ObjectMapper mapper = new ObjectMapper();
                                                       try {
                                                           listConsumer.accept(mapper.readValue(
                                                                   response.toString(),
                                                                   new TypeReference<List<GroceryListData>>() {
                                                                   }));
                                                       } catch (IOException e) {
                                                           e.printStackTrace();
                                                       }
                                                   },
                                                   error -> backend.onErrorHandler(error,
                                                                                   errorListener,
                                                                                   R.string.loading_failed),
                                                   backend.token);

        backend.queue.add(json);
    }

    public void createGroceryList(GroceryListData groceryList,
                                  Consumer<String> onSuccess,
                                  Response.ErrorListener errorListener) {
        Map<String, String> postParams = new HashMap<>();
        postParams.put("name", groceryList.getName());

        ApiPostRequest request = new ApiPostRequest(backend.url + "/api/grocery-list/new",
                                                    onSuccess::accept,
                                                    error -> backend.onErrorHandler(error,
                                                                                    errorListener,
                                                                                    R.string.loading_failed),
                                                    backend.token,
                                                    postParams);
        backend.queue.add(request);
    }

    public void updateGroceryList(GroceryList list,
                                  Response.Listener<String> onSuccess,
                                  Response.ErrorListener errorListener) {
        String requestUrl = backend.url + "/api/grocery-list/" + list.getId();
        ApiPostRequest request = new ApiPostRequest(requestUrl,
                                                    onSuccess,
                                                    errorListener,
                                                    backend.token,
                                                    list.toData().toMap());
        backend.queue.add(request);
    }

    public void deleteGroceryList(GroceryList list,
                                  Response.Listener<String> onSuccess,
                                  Response.ErrorListener errorListener) {
        String requestUrl = backend.url + "/api/grocery-list/" + list.getId() + "/delete";
        ApiDeleteRequest request =
                new ApiDeleteRequest(requestUrl, onSuccess, errorListener, backend.token);
        backend.queue.add(request);
    }
}
