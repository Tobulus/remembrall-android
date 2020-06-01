package com.remembrall.api.request;

import androidx.annotation.Nullable;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ApiObjectRequest extends JsonObjectRequest {

    private String token;

    public ApiObjectRequest(String url,
                            @Nullable JSONObject jsonRequest,
                            Response.Listener<JSONObject> listener,
                            @Nullable Response.ErrorListener errorListener,
                            String token) {
        super(url, jsonRequest, listener, errorListener);
        this.token = token;
    }

    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> params = new HashMap<>();
        params.put("X-AUTH-TOKEN", token);
        return params;
    }
}
