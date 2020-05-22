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

public class CreateInvitationActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_invitation);

        Long groceryListId = getIntent().getExtras().getLong("id");

        final TextView name = findViewById(R.id.email);
        final Button save = findViewById(R.id.invite);
        save.setEnabled(true);

        save.setOnClickListener(v -> {
            GroceryListData groceryList = new GroceryListData();
            groceryList.setName(name.getText().toString());
            ServiceLocator.getInstance()
                          .get(Backend.class)
                          .createInvitation(groceryListId,
                                            name.getText().toString(),
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
