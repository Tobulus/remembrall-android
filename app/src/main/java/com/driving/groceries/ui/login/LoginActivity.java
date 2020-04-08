package com.driving.groceries.ui.login;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.driving.groceries.R;

import org.json.JSONException;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class LoginActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        CookieManager manager = new CookieManager();
        CookieHandler.setDefault(manager);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        RequestQueue queue = Volley.newRequestQueue(this);
        Backend.initBackend(getApplicationContext());
        loginButton.setEnabled(true);

        loginButton.setOnClickListener(v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);

            AtomicReference<String> token = new AtomicReference<>();
            JsonObjectRequest csrf = new JsonObjectRequest("http://192.168.0.249:8080/service/json/csrf", null, response -> {
                // TODO: persistence
                try {
                    token.set(response.getString("_csrf.token"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                StringRequest login = new StringRequest(Request.Method.POST, "http://192.168.0.249:8080/login", r -> {
                    loadingProgressBar.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(this, GroceryListsActivity.class);
                    startActivity(intent);
                    finish();
                }, error -> Toast.makeText(getApplicationContext(), "Failed:" + error.getMessage(), Toast.LENGTH_LONG).show()) {
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<>();
                        params.put("username", usernameEditText.getText().toString());
                        params.put("password", passwordEditText.getText().toString());
                        params.put("_csrf", token.get());
                        return params;
                    }
                };
                queue.add(login);
            }, error -> Toast.makeText(getApplicationContext(), "Failed:" + error.getMessage(), Toast.LENGTH_LONG).show());

            queue.add(csrf);
        });
    }
}
