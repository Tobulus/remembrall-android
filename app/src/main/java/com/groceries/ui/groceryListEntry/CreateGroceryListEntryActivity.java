package com.groceries.ui.groceryListEntry;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.groceries.R;
import com.groceries.api.Backend;
import com.groceries.model.GroceryList;
import com.groceries.model.GroceryListEntry;

public class CreateGroceryListEntryActivity extends AppCompatActivity {

    private Backend backend;
    private Long groceryListId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_grocerylist_entry);

        backend = new Backend(getApplicationContext());
        groceryListId = getIntent().getExtras().getLong("id");

        final TextView name = findViewById(R.id.name);
        final Button save = findViewById(R.id.send);
        save.setEnabled(true);

        save.setOnClickListener(v -> {
            GroceryListEntry entry = new GroceryListEntry();
            entry.setName(name.getText().toString());
            backend.createGroceryListEntry(groceryListId, entry,
                    json -> finish(),
                    error -> Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show());
        });
    }
}
