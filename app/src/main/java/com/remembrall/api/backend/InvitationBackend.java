package com.remembrall.api.backend;

import com.android.volley.Response;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.remembrall.R;
import com.remembrall.api.data.InvitationData;
import com.remembrall.api.request.ApiArrayRequest;
import com.remembrall.api.request.ApiPostRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class InvitationBackend {

    private final Backend backend;

    public InvitationBackend(Backend backend) {
        this.backend = backend;
    }

    public void createInvitation(Long groceryListId,
                                 String email,
                                 Consumer<String> onSuccess,
                                 Response.ErrorListener errorListener) {
        Map<String, String> postParams = new HashMap<>();
        postParams.put("email", email);

        ApiPostRequest request =
                new ApiPostRequest(backend.url + "/api/grocery-list/" + groceryListId + "/invite",
                                   onSuccess::accept,
                                   error -> backend.onErrorHandler(error,
                                                                   errorListener,
                                                                   R.string.loading_failed),
                                   backend.token,
                                   postParams);
        backend.queue.add(request);
    }

    public void getInvitations(Consumer<List<InvitationData>> listConsumer,
                               Response.ErrorListener errorListener) {
        ApiArrayRequest json = new ApiArrayRequest(backend.url + "/api/invitations",
                                                   response -> {
                                                       ObjectMapper mapper = new ObjectMapper();
                                                       try {
                                                           listConsumer.accept(mapper.readValue(
                                                                   response.toString(),
                                                                   new TypeReference<List<InvitationData>>() {
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

    public void acknowledge(Long invitationId, Response.Listener<String> onSuccess) {
        String requestUrl = backend.url + "/api/invitation/" + invitationId;
        Map<String, String> params = new HashMap<>();
        params.put("ack", "true");
        ApiPostRequest request =
                new ApiPostRequest(requestUrl, onSuccess, null, backend.token, params);
        backend.queue.add(request);
    }

    public void deny(Long invitationId, Response.Listener<String> onSuccess) {
        String requestUrl = backend.url + "/api/invitation/" + invitationId;
        Map<String, String> params = new HashMap<>();
        params.put("deny", "true");
        ApiPostRequest request =
                new ApiPostRequest(requestUrl, onSuccess, null, backend.token, params);
        backend.queue.add(request);
    }
}
