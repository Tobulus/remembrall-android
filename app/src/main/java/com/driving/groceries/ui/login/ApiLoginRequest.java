package com.driving.groceries.ui.login;

import androidx.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class ApiLoginRequest extends JsonObjectRequest {

    private String token;
    private String user;
    private String password;

    public ApiLoginRequest(String url, String user, String password, Response.Listener<JSONObject> listener, @Nullable Response.ErrorListener errorListener) {
        super(url, null, listener, errorListener);
        this.user = user;
        this.password = password;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        token = response.headers.get("X-AUTH-TOKEN");
        return super.parseNetworkResponse(response);
    }

    @Override
    protected Map<String,String> getParams(){
        Map<String,String> params = new HashMap<>();
        String phrase = user + ":" + password;
        params.put("X-AUTH-TOKEN", "(empty)");
        params.put("Authorization", "Basic " + Base64.getEncoder().encodeToString(phrase.getBytes()));
        return params;
    }

    public String getToken() {
        return token;
    }
}
