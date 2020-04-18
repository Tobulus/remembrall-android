package com.groceries.ui.groceryList;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.groceries.R;
import com.groceries.api.Backend;
import com.groceries.model.GroceryList;

public class CreateGroceryListActivity extends AppCompatActivity {

    private Backend backend;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_grocerylist);

        backend = new Backend(getApplicationContext());

        final TextView name = findViewById(R.id.name);
        final Button save = findViewById(R.id.send);
        save.setEnabled(true);

        save.setOnClickListener(v -> {
            GroceryList groceryList = new GroceryList();
            groceryList.setName(name.getText().toString());
            backend.createGroceryList(groceryList,
                    json -> finish(),
                    error -> Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show());
        });
    }
}
