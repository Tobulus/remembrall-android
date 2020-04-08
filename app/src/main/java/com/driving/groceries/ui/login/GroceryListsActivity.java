package com.driving.groceries.ui.login;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.driving.groceries.R;

public class GroceryListsActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocerylists);
    }

    @Override
    public void onListFragmentInteraction(GroceryList item) {
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }
}
