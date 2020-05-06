package com.groceries.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import com.android.volley.Response;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.groceries.R;
import com.groceries.api.Backend;
import com.groceries.model.GroceryList;
import com.groceries.model.GroceryListEntry;
import com.groceries.model.Invitation;
import com.groceries.ui.groceryList.GroceryListFragment;
import com.groceries.ui.groceryListEntry.GroceryListEntryFragment;
import com.groceries.ui.invitation.InvitationFragment;

import java.util.List;
import java.util.function.Consumer;

public class GroceriesActivity extends AppCompatActivity
        implements GroceryListFragment.GroceryListFragmentInteractionListener,
                   GroceryListEntryFragment.GroceryListEntryFragmentInteractionListener,
                   InvitationFragment.InvitationFragmentInteractionListener {

    private Backend backend;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        backend = new Backend(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocerylists);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.inflateMenu(R.menu.main_navigation_bottom);
        navigation.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.action_lists:
                    showGroceryLists();
                    break;
                case R.id.action_invitations:
                    showInvitations();
                    break;
            }
            return true;
        });

        if (findViewById(R.id.grocery_fragment) != null) {
            if (savedInstanceState != null) {
                return;
            }

            GroceryListFragment groceryListFragment = new GroceryListFragment();
            getSupportFragmentManager().beginTransaction()
                                       .add(R.id.grocery_fragment, groceryListFragment)
                                       .commit();
        }
    }

    private void showInvitations() {
        InvitationFragment fragment = new InvitationFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.grocery_fragment, fragment);
        //transaction.addToBackStack("showLists");
        transaction.commit();
    }

    private void showGroceryLists() {
        GroceryListFragment fragment = new GroceryListFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.grocery_fragment, fragment);
        //transaction.addToBackStack("showInvitations");
        transaction.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

    @Override
    public void ackInvitation(Invitation item) {

    }

    @Override
    public void denyInvitation(Invitation item) {

    }

    @Override
    public void loadInvitations(Consumer<List<Invitation>> listConsumer,
                                Response.ErrorListener errorListener) {
        backend.getInvitations(listConsumer, errorListener);
    }
}
