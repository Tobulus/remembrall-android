package com.groceries.ui;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.groceries.R;
import com.groceries.api.Backend;

public class LoginActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Backend.initBackend(getApplicationContext());

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        loginButton.setEnabled(true);

        loginButton.setOnClickListener(v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);

            Backend.get().login(usernameEditText.getText().toString(), passwordEditText.getText().toString(), json -> {
                loadingProgressBar.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(this, GroceryListsActivity.class);
                startActivity(intent);
                finish();
            }, error -> Toast.makeText(getApplicationContext(), "Failed:" + error.getMessage(), Toast.LENGTH_LONG).show());
        });
    }
}
