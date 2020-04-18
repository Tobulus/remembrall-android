package com.groceries.ui.groceryListEntry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.groceries.R;
import com.groceries.model.GroceryList;
import com.groceries.model.GroceryListEntry;
import com.groceries.ui.groceryList.CreateGroceryListActivity;
import com.groceries.ui.groceryList.GroceryListsActivity;

public class GroceryListEntriesActivity extends AppCompatActivity implements GroceryListEntryFragment.OnGroceryListEntryFragmentInteractionListener {

    private Long groceryListId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocerylistentries);

        groceryListId = getIntent().getExtras().getLong("id");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(GroceryListEntriesActivity.this, CreateGroceryListEntryActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong("id", groceryListId);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    @Override
    public void onClickGroceryListEntry(GroceryListEntry entry) {
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }
}
