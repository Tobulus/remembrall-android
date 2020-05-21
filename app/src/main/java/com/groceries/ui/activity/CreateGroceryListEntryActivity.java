package com.groceries.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.groceries.R;
import com.groceries.api.Backend;
import com.groceries.api.data.GroceryListEntryData;

public class CreateGroceryListEntryActivity extends AppCompatActivity {

    private Backend backend;
    private Long groceryListId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_grocerylist_entry);

        // TODO:
        backend = new Backend(getApplicationContext(), null);
        groceryListId = getIntent().getExtras().getLong("id");

        final TextView name = findViewById(R.id.name);
        final Button save = findViewById(R.id.send);
        save.setEnabled(true);

        save.setOnClickListener(v -> {
            GroceryListEntryData entry = new GroceryListEntryData();
            entry.setName(name.getText().toString());
            backend.createGroceryListEntry(groceryListId,
                                           entry,
                                           json -> {
                                               setResult(Activity.RESULT_OK);
                                               finish();
                                           },
                                           error -> Toast.makeText(getApplicationContext(),
                                                                   error.getMessage(),
                                                                   Toast.LENGTH_LONG).show());
        });
    }
}
