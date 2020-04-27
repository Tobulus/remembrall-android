package com.groceries.ui.basic;

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

public class RegistrationActivity extends AppCompatActivity {

    private Backend backend;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        backend = new Backend(getApplicationContext());

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final EditText matchingPasswordEditText = findViewById(R.id.matchingPassword);

        final Button loginButton = findViewById(R.id.register);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        loginButton.setEnabled(true);
        loginButton.setOnClickListener(v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);

            backend.register(usernameEditText.getText().toString(),
                             passwordEditText.getText().toString(),
                             matchingPasswordEditText.getText().toString(),
                             json -> {
                                 loadingProgressBar.setVisibility(View.INVISIBLE);
                                 Intent intent = new Intent(this, LoginActivity.class);
                                 startActivity(intent);
                                 finish();
                             },
                             error -> Toast.makeText(getApplicationContext(),
                                                     "Failed:" + error.getMessage(),
                                                     Toast.LENGTH_LONG).show());
        });
    }
}
