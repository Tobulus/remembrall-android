package com.groceries.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.groceries.R;
import com.groceries.api.Backend;
import com.groceries.api.BackendProvider;
import com.groceries.model.GroceryList;
import com.groceries.model.GroceryListEntry;
import com.groceries.ui.groceryList.GroceryListFragment;
import com.groceries.ui.groceryListEntry.GroceryListEntryFragment;
import com.groceries.ui.invitation.InvitationFragment;
import com.groceries.ui.login_registration.LoginFragment;
import com.groceries.ui.login_registration.LoginRegistrationFragment;
import com.groceries.ui.login_registration.RegistrationFragment;

public class MainActivity extends AppCompatActivity
        implements BackendProvider, GroceryListFragment.GroceryListListener,
                   GroceryListEntryFragment.GroceryListEntryListener, LoginFragment.LoginListener,
                   RegistrationFragment.RegistrationListener, LoginRequiredListener {

    private Backend backend;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        backend = new Backend(getApplicationContext(), this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        initNavigation();

        if (!backend.restoreSession()) {
            showLoginRegistration();
            return;
        }

        loginFulfilled();

        if (findViewById(R.id.main_fragment) != null) {
            if (savedInstanceState != null) {
                return;
            }

            GroceryListFragment groceryListFragment = new GroceryListFragment();
            getSupportFragmentManager().beginTransaction()
                                       .add(R.id.main_fragment, groceryListFragment)
                                       .commit();
        }
    }

    private void loginFulfilled() {
        findViewById(R.id.navigation).setVisibility(View.VISIBLE);
        findViewById(R.id.floating_plus_button).setVisibility(View.VISIBLE);
        findViewById(R.id.main_fragment).setVisibility(View.VISIBLE);
        findViewById(R.id.view_pager).setVisibility(View.GONE);
    }

    private void initNavigation() {
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
    }

    private void showLoginRegistration() {
        LoginRegistrationFragment fragment = new LoginRegistrationFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment, fragment);
        transaction.commit();
    }

    private void showLogin() {
        LoginFragment fragment = new LoginFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment, fragment);
        transaction.commit();
    }

    private void showInvitations() {
        InvitationFragment fragment = new InvitationFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment, fragment);
        transaction.commit();
    }

    private void showGroceryLists() {
        GroceryListFragment fragment = new GroceryListFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment, fragment);
        transaction.commit();
    }

    private void showGroceryListEntries(Long listId) {
        Bundle bundle = new Bundle();
        GroceryListEntryFragment fragment = new GroceryListEntryFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        bundle.putLong("id", listId);
        fragment.setArguments(bundle);

        transaction.replace(R.id.main_fragment, fragment);
        transaction.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    @Override
    public Backend getBackend() {
        return backend;
    }

    @Override
    public void onClick(GroceryList item) {
        showGroceryListEntries(item.getId());
    }

    @Override
    public void onClick(GroceryListEntry entry) {

    }

    @Override
    public void onLoginComplete() {
        loginFulfilled();
        showGroceryLists();
    }

    @Override
    public void onRegistrationComplete() {
        ViewPager pager = findViewById(R.id.view_pager);
        pager.setCurrentItem(0);
    }

    @Override
    public void onLoginRequired() {
        showLogin();
    }
}
