package com.groceries.api;

import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class ApiArrayRequest extends JsonArrayRequest {

    private String token;

    public ApiArrayRequest(String url, Response.Listener<JSONArray> listener, @Nullable Response.ErrorListener errorListener, String token) {
        super(url, listener, errorListener);
        this.token = token;
    }

    @Override
    protected Map<String,String> getParams(){
        Map<String,String> params = new HashMap<>();
        params.put("X-AUTH-TOKEN", token);
        return params;
    }
}
