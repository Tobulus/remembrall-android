package com.remembrall.api.backend;

import com.android.volley.Response;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.remembrall.api.data.GroceryListEntryData;
import com.remembrall.api.request.ApiArrayRequest;
import com.remembrall.api.request.ApiDeleteRequest;
import com.remembrall.api.request.ApiPostRequest;
import com.remembrall.model.database.GroceryListEntry;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class GroceryListEntryBackend {

    private final Backend backend;

    public GroceryListEntryBackend(Backend backend) {
        this.backend = backend;
    }

    public void getGroceryListEntries(Long groceryList,
                                      Consumer<List<GroceryListEntryData>> listConsumer,
                                      Response.ErrorListener errorListener) {
        String requestUrl = backend.url + "/api/grocery-list/" + groceryList + "/entries";

        ApiArrayRequest json = new ApiArrayRequest(requestUrl, response -> {
            ObjectMapper mapper = new ObjectMapper();
            try {
                listConsumer.accept(mapper.readValue(response.toString(),
                                                     new TypeReference<List<GroceryListEntryData>>() {
                                                     }));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, error -> backend.onErrorHandler(error, errorListener), backend.token);

        backend.queue.add(json);
    }

    public void updateGroceryListEntry(Long groceryListId,
                                       GroceryListEntry entry,
                                       Response.Listener<String> onSuccess,
                                       Response.ErrorListener errorListener) {
        String requestUrl =
                backend.url + "/api/grocery-list/" + groceryListId + "/entry/" + entry.getId();
        ApiPostRequest request = new ApiPostRequest(requestUrl,
                                                    onSuccess,
                                                    errorListener,
                                                    backend.token,
                                                    entry.toData().toMap());
        backend.queue.add(request);
    }

    public void deleteGroceryListEntry(Long listId,
                                       GroceryListEntry entry,
                                       Response.Listener<String> onSuccess,
                                       Response.ErrorListener errorListener) {
        String requestUrl =
                backend.url + "/api/grocery-list/" + listId + "/entry/" + entry.getId() + "/delete";
        ApiDeleteRequest request =
                new ApiDeleteRequest(requestUrl, onSuccess, errorListener, backend.token);
        backend.queue.add(request);
    }

    public void createGroceryListEntry(Long groceryListId,
                                       GroceryListEntryData entry,
                                       Consumer<String> onSuccess,
                                       Response.ErrorListener errorListener) {
        String requestUrl = backend.url + "/api/grocery-list/" + groceryListId + "/entry/new";
        ApiPostRequest request = new ApiPostRequest(requestUrl,
                                                    onSuccess::accept,
                                                    error -> backend.onErrorHandler(error,
                                                                                    errorListener),
                                                    backend.token,
                                                    entry.toMap());
        backend.queue.add(request);
    }
}
