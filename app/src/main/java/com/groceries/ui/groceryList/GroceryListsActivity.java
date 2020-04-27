package com.groceries.ui.groceryList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Response;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.groceries.R;
import com.groceries.api.Backend;
import com.groceries.model.GroceryList;
import com.groceries.ui.groceryListEntry.GroceryListEntriesActivity;

import java.util.List;
import java.util.function.Consumer;

public class GroceryListsActivity extends AppCompatActivity
        implements GroceryListFragment.GroceryListFragmentInteractionListener {

    private static final int LAUNCH_CREATE_ACTIVITY = 1;

    private Backend backend;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        backend = new Backend(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocerylists);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(GroceryListsActivity.this, CreateGroceryListActivity.class);
            startActivityForResult(intent, LAUNCH_CREATE_ACTIVITY);
        });
    }

    @Override
    public void onClickGroceryList(GroceryList list) {
        Intent intent = new Intent(this, GroceryListEntriesActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong("id", list.getId());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void loadGroceryLists(Consumer<List<GroceryList>> listConsumer,
                                 Response.ErrorListener errorListener) {
        backend.getGroceryLists(listConsumer, errorListener);
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
    public void onPointerCaptureChanged(boolean hasCapture) {
    }
}
