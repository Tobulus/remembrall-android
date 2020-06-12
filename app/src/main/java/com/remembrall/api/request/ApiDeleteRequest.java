package com.remembrall.api.request;

import androidx.annotation.Nullable;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ApiDeleteRequest extends StringRequest {

    private String token;

    public ApiDeleteRequest(String url,
                            Response.Listener<String> listener,
                            @Nullable Response.ErrorListener errorListener,
                            String token) {
        super(Method.DELETE, url, listener, errorListener);
        this.token = token;
    }

    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> params = new HashMap<>();
        params.put("X-AUTH-TOKEN", token);
        return params;
    }
}
