package com.groceries.ui.groceryList;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.groceries.R;
import com.groceries.model.GroceryList;

public class GroceryListsActivity extends AppCompatActivity implements GroceryListFragment.OnGroceryListFragmentInteractionListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocerylists);
    }

    @Override
    public void onClickGroceryList(GroceryList list) {
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }
}
