package com.groceries.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import com.android.volley.Response;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.groceries.R;
import com.groceries.api.Backend;
import com.groceries.model.GroceryList;
import com.groceries.model.GroceryListEntry;
import com.groceries.ui.groceryList.GroceryListFragment;
import com.groceries.ui.groceryListEntry.GroceryListEntryFragment;

import java.util.List;
import java.util.function.Consumer;

public class GroceriesActivity extends AppCompatActivity
        implements GroceryListFragment.GroceryListFragmentInteractionListener,
                   GroceryListEntryFragment.GroceryListEntryFragmentInteractionListener {

    private static final int LAUNCH_CREATE_ACTIVITY = 1;

    private Backend backend;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        backend = new Backend(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocerylists);

        if (findViewById(R.id.grocery_fragment) != null) {
            if (savedInstanceState != null) {
                return;
            }

            GroceryListFragment groceryListFragment = new GroceryListFragment();
            getSupportFragmentManager().beginTransaction()
                                       .add(R.id.grocery_fragment, groceryListFragment)
                                       .commit();

            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(view -> {
                Intent intent =
                        new Intent(GroceriesActivity.this, CreateGroceryListActivity.class);
                startActivityForResult(intent, LAUNCH_CREATE_ACTIVITY);
            });
        }
    }

    @Override
    public void onClickGroceryList(GroceryList list) {
        Bundle bundle = new Bundle();
        bundle.putLong("id", list.getId());

        GroceryListEntryFragment fragment = new GroceryListEntryFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragment.setArguments(bundle);
        transaction.replace(R.id.grocery_fragment, fragment);
        transaction.addToBackStack("showListEntries");
        transaction.commit();
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

    @Override
    public void onClick(GroceryListEntry entry) {
    }

    @Override
    public void toggleChecked(GroceryListEntry entry, Runnable onError) {
        entry.setChecked(!entry.isChecked());
        backend.updateGroceryListEntry(entry.getGroceryList().getId(), entry, s -> {
        }, e -> onError.run());
    }

    @Override
    public void loadListEntries(Long groceryListId,
                                Consumer<List<GroceryListEntry>> listConsumer,
                                Response.ErrorListener errorListener) {
        backend.getGroceryListEntries(groceryListId, listConsumer, errorListener);
    }
}
