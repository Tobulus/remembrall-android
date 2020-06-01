package com.remembrall.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.remembrall.R;
import com.remembrall.api.Backend;
import com.remembrall.api.data.GroceryListEntryData;
import com.remembrall.locator.ServiceLocator;

public class CreateGroceryListEntryActivity extends AppCompatActivity {
    private Long groceryListId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_grocerylist_entry);

        groceryListId = getIntent().getExtras().getLong("id");

        final TextView name = findViewById(R.id.name);
        final Button save = findViewById(R.id.send);
        save.setEnabled(true);

        save.setOnClickListener(v -> {
            GroceryListEntryData entry = new GroceryListEntryData();
            entry.setName(name.getText().toString());
            ServiceLocator.getInstance()
                          .get(Backend.class)
                          .createGroceryListEntry(groceryListId, entry,
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
