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

public class GroceryListEntriesActivity extends AppCompatActivity implements GroceryListEntryFragment.OnGroceryListEntryFragmentInteractionListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocerylistentries);
    }

    @Override
    public void onClickGroceryListEntry(GroceryListEntry entry) {
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }
}
