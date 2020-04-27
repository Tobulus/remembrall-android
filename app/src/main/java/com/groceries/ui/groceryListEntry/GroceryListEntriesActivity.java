package com.groceries.ui.groceryListEntry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Response;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.groceries.R;
import com.groceries.api.Backend;
import com.groceries.model.GroceryListEntry;

import java.util.List;
import java.util.function.Consumer;

public class GroceryListEntriesActivity extends AppCompatActivity
        implements GroceryListEntryFragment.GroceryListEntryFragmentInteractionListener {

    private static final int LAUNCH_CREATE_ACTIVITY = 1;

    private Long groceryListId;

    private Backend backend;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        backend = new Backend(getApplicationContext());
        groceryListId = getIntent().getExtras().getLong("id");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocerylistentries);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(GroceryListEntriesActivity.this,
                                       CreateGroceryListEntryActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong("id", groceryListId);
            intent.putExtras(bundle);
            startActivityForResult(intent, LAUNCH_CREATE_ACTIVITY);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_CREATE_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                Intent intent = getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                startActivity(intent);
            }
        }
    }

    @Override
    public void onClick(GroceryListEntry entry) {
    }

    @Override
    public void toggleChecked(GroceryListEntry entry, Runnable onError) {
        entry.setChecked(!entry.isChecked());
        backend.updateGroceryListEntry(groceryListId, entry, s -> {
        }, e -> onError.run());
    }

    @Override
    public void loadListEntries(Consumer<List<GroceryListEntry>> listConsumer,
                                Response.ErrorListener errorListener) {
        backend.getGroceryListEntries(groceryListId, listConsumer, errorListener);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }
}
