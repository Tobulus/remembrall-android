package com.remembrall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.remembrall.R;
import com.remembrall.api.Backend;
import com.remembrall.api.NetworkResponseHandler;
import com.remembrall.listener.BackPressedListener;
import com.remembrall.listener.LoginRequiredListener;
import com.remembrall.locator.ServiceLocator;
import com.remembrall.model.database.GroceryList;
import com.remembrall.ui.groceryList.GroceryListDialog;
import com.remembrall.ui.groceryList.GroceryListFragment;
import com.remembrall.ui.groceryListEntry.GroceryListEntryDialog;
import com.remembrall.ui.groceryListEntry.GroceryListEntryFragment;
import com.remembrall.ui.invitation.InvitationFragment;
import com.remembrall.ui.login_registration.LoginFragment;
import com.remembrall.ui.login_registration.LoginRegistrationFragment;
import com.remembrall.ui.login_registration.RegistrationFragment;

import static com.remembrall.ui.groceryList.GroceryListFragment.LAUNCH_CREATE_GROCERY_LIST;
import static com.remembrall.ui.groceryListEntry.GroceryListEntryFragment.LAUNCH_CREATE_GROCERY_LIST_ENTRY;

public class MainActivity extends AppCompatActivity
        implements GroceryListFragment.GroceryListListener, LoginFragment.LoginListener,
                   RegistrationFragment.RegistrationListener, LoginRequiredListener {

    /* Define the names of different Intent which this activity accepts*/
    public static final String INTENT_NAME = "action";

    /* Define codes for intent actions which this activity accepts*/
    public static final int INTENT_ACTION_SHOW_INVITATIONS = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        ServiceLocator.getInstance().get(NetworkResponseHandler.class).register(this);

        initNavigation();

        if (!ServiceLocator.getInstance().get(Backend.class).isSessionAvailable()) {
            showLoginRegistration();
            return;
        }

        loginFulfilled();

        if (findViewById(R.id.main_fragment) != null) {
            if (savedInstanceState != null) {
                return;
            }

            showGroceryLists(false);
        }
    }

    private void loginFulfilled() {
        findViewById(R.id.navigation).setVisibility(View.VISIBLE);
        findViewById(R.id.floating_plus_button).setVisibility(View.VISIBLE);
    }

    private void initNavigation() {
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.action_lists:
                    showGroceryLists(false);
                    break;
                case R.id.action_archived_lists:
                    showGroceryLists(true);
                    break;
                case R.id.action_invitations:
                    showInvitations();
                    break;
            }
            return true;
        });
    }

    private void showLoginRegistration() {
        findViewById(R.id.navigation).setVisibility(View.INVISIBLE);
        findViewById(R.id.floating_plus_button).setVisibility(View.INVISIBLE);
        LoginRegistrationFragment fragment = new LoginRegistrationFragment();
        getSupportFragmentManager().beginTransaction()
                                   .replace(R.id.main_fragment, fragment)
                                   .commit();
    }

    private void showInvitations() {
        InvitationFragment fragment = new InvitationFragment();
        getSupportFragmentManager().beginTransaction()
                                   .replace(R.id.main_fragment, fragment)
                                   .addToBackStack("invitations")
                                   .commit();
        findViewById(R.id.floating_plus_button).setVisibility(View.INVISIBLE);
    }

    private void showGroceryLists(boolean archived) {
        GroceryListFragment fragment = new GroceryListFragment(archived);
        getSupportFragmentManager().beginTransaction()
                                   .replace(R.id.main_fragment, fragment)
                                   .addToBackStack("lists")
                                   .commit();

        findViewById(R.id.floating_plus_button).setVisibility(archived ?
                                                              View.INVISIBLE :
                                                              View.VISIBLE);
        FloatingActionButton fab = findViewById(R.id.floating_plus_button);
        fab.setOnClickListener(v -> {
            GroceryListDialog dialog = new GroceryListDialog();
            dialog.setTargetFragment(fragment, LAUNCH_CREATE_GROCERY_LIST);
            dialog.show(getSupportFragmentManager(), "dialog-grocery-list");
        });
    }

    private void showGroceryListEntries(Long listId) {
        Bundle bundle = new Bundle();
        bundle.putLong("id", listId);
        GroceryListEntryFragment fragment = new GroceryListEntryFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                                   .replace(R.id.main_fragment, fragment)
                                   .addToBackStack("entries")
                                   .commit();

        findViewById(R.id.floating_plus_button).setVisibility(View.VISIBLE);
        FloatingActionButton fab = findViewById(R.id.floating_plus_button);
        fab.setOnClickListener(v -> {
            GroceryListEntryDialog dialog = new GroceryListEntryDialog();
            Bundle dialogBundle = new Bundle();
            dialogBundle.putLong("id", listId);
            dialog.setArguments(dialogBundle);
            dialog.setTargetFragment(fragment, LAUNCH_CREATE_GROCERY_LIST_ENTRY);
            dialog.show(getSupportFragmentManager(), "create-grocery-list-entry");
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    @Override
    public void onBackPressed() {
        boolean processed = getSupportFragmentManager().getFragments()
                                                       .stream()
                                                       .filter(fragment -> fragment instanceof BackPressedListener
                                                                           && fragment.isVisible())
                                                       .map(fragment -> ((BackPressedListener) fragment)
                                                               .onBackPressed())
                                                       .reduce(false, (a, b) -> a || b);
        if (!processed) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.getExtras() != null
            && intent.getExtras().getInt(INTENT_NAME) == INTENT_ACTION_SHOW_INVITATIONS) {
            showInvitations();
        }

        super.onNewIntent(intent);
    }

    @Override
    public void onClick(GroceryList item) {
        showGroceryListEntries(item.getId());
    }

    @Override
    public void onLoginComplete() {
        loginFulfilled();
        showGroceryLists(false);
    }

    @Override
    public void onRegistrationComplete() {
        ViewPager pager = findViewById(R.id.view_pager);
        pager.setCurrentItem(0);
    }

    @Override
    public void onLoginRequired() {
        showLoginRegistration();
    }
}
