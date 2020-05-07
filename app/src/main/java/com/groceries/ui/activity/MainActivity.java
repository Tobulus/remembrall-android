package com.groceries.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import com.android.volley.Response;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.groceries.R;
import com.groceries.api.Backend;
import com.groceries.model.GroceryList;
import com.groceries.model.GroceryListEntry;
import com.groceries.model.Invitation;
import com.groceries.ui.groceryList.GroceryListFragment;
import com.groceries.ui.groceryListEntry.GroceryListEntryFragment;
import com.groceries.ui.invitation.InvitationFragment;
import com.groceries.ui.login.LoginFragment;
import com.groceries.ui.registration.RegistrationFragment;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MainActivity extends AppCompatActivity
        implements GroceryListFragment.GroceryListFragmentInteractionListener,
                   GroceryListEntryFragment.GroceryListEntryFragmentInteractionListener,
                   InvitationFragment.InvitationFragmentInteractionListener,
                   LoginFragment.LoginFragmentInteractionListener,
                   RegistrationFragment.RegistrationFragmentInteractionListener {

    private Backend backend;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        backend = new Backend(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        if (!backend.restoreSession()) {
            showLogin();
            return;
        }

        navigation.setVisibility(View.VISIBLE);
        findViewById(R.id.floating_plus_button).setVisibility(View.VISIBLE);
        findViewById(R.id.navigation).setVisibility(View.VISIBLE);
        findViewById(R.id.view_pager).setVisibility(View.INVISIBLE);

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

    private void showLogin() {
        ViewPager pager = findViewById(R.id.view_pager);
        TabLayout tabs = findViewById(R.id.view_pager_tabs);
        LoginFragment loginFragment = new LoginFragment();
        RegistrationFragment registrationFragment = new RegistrationFragment();
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        tabs.setupWithViewPager(pager);

        adapter.add(loginFragment);
        adapter.add(registrationFragment);
        pager.setAdapter(adapter);
        pager.setVisibility(View.VISIBLE);
    }

    private void showInvitations() {
        InvitationFragment fragment = new InvitationFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment, fragment);
        //transaction.addToBackStack("showLists");
        transaction.commit();
    }

    private void showGroceryLists() {
        GroceryListFragment fragment = new GroceryListFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment, fragment);
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
        transaction.replace(R.id.main_fragment, fragment);
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

    @Override
    public void login(String username,
                      String password,
                      Response.Listener<JSONObject> onSuccess,
                      Response.ErrorListener onError) {
        backend.login(username, password, onSuccess, onError);
    }

    @Override
    public void register(String user,
                         String password,
                         String matchingPassword,
                         Consumer<String> onSuccess,
                         Response.ErrorListener errorListener) {
        backend.register(user, password, matchingPassword, onSuccess, errorListener);
    }

    class PagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments = new ArrayList<>();

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        void add(Fragment fragment) {
            this.fragments.add(fragment);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "Login";
            } else {
                return "Registration";
            }
        }
    }
}
