package com.groceries.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.groceries.R;
import com.groceries.api.Backend;
import com.groceries.api.data.GroceryListData;
import com.groceries.locator.ServiceLocator;

public class CreateGroceryListActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_grocerylist);

        final TextView name = findViewById(R.id.name);
        final Button save = findViewById(R.id.send);
        save.setEnabled(true);

        save.setOnClickListener(v -> {
            GroceryListData groceryList = new GroceryListData();
            groceryList.setName(name.getText().toString());
            ServiceLocator.getInstance().get(Backend.class).createGroceryList(groceryList,
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
