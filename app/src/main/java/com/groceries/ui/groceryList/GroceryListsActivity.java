package com.groceries.ui.groceryList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.groceries.R;
import com.groceries.model.GroceryList;
import com.groceries.ui.groceryListEntry.GroceryListEntriesActivity;

public class GroceryListsActivity extends AppCompatActivity implements GroceryListFragment.OnGroceryListFragmentInteractionListener {

    private static final int LAUNCH_CREATE_ACTIVITY = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_CREATE_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK){
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
