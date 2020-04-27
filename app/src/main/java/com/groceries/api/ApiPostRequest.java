package com.groceries.api;

import androidx.annotation.Nullable;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ApiPostRequest extends StringRequest {

    private String token;
    private Map<String, String> postParams;

    public ApiPostRequest(String url,
                          Response.Listener<String> listener,
                          @Nullable Response.ErrorListener errorListener,
                          String token,
                          Map<String, String> postParams) {
        super(Method.POST, url, listener, errorListener);
        this.token = token;
        this.postParams = postParams;
    }

    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> params = new HashMap<>();
        params.put("X-AUTH-TOKEN", token);
        return params;
    }

    @Override
    protected Map<String, String> getParams() {
        return postParams;
    }
}
