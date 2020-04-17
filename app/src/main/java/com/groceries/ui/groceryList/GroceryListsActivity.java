package com.groceries.ui.groceryList;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.groceries.R;
import com.groceries.model.GroceryList;
import com.groceries.ui.groceryListEntry.GroceryListEntriesActivity;

public class GroceryListsActivity extends AppCompatActivity implements GroceryListFragment.OnGroceryListFragmentInteractionListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocerylists);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(GroceryListsActivity.this, CreateGroceryListActivity.class);
            startActivity(intent);
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
    public void onPointerCaptureChanged(boolean hasCapture) {
    }
}
