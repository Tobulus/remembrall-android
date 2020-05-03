package com.groceries.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.groceries.R;
import com.groceries.api.Backend;

public class LoginActivity extends AppCompatActivity {

    private Backend backend;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backend = new Backend(getApplicationContext());

        if (backend.restoreSession()) {
            switchToGroceryLists();
            return;
        }

        setContentView(R.layout.activity_login);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final Button registerButton = findViewById(R.id.register);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        registerButton.setEnabled(true);
        loginButton.setEnabled(true);

        loginButton.setOnClickListener(v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);

            backend.login(usernameEditText.getText().toString(),
                          passwordEditText.getText().toString(),
                          json -> {
                              loadingProgressBar.setVisibility(View.INVISIBLE);
                              switchToGroceryLists();
                          },
                          error -> {
                              Toast.makeText(getApplicationContext(),
                                             "Failed:" + error.getMessage(),
                                             Toast.LENGTH_LONG).show();
                          });
        });

        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });
    }

    private void switchToGroceryLists() {
        Intent intent = new Intent(this, GroceriesActivity.class);
        startActivity(intent);
        finish();
    }
}
