package com.remembrall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.remembrall.R;
import com.remembrall.api.NetworkResponseHandler;
import com.remembrall.api.backend.Backend;
import com.remembrall.listener.BackPressedListener;
import com.remembrall.listener.FailedListener;
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
import com.remembrall.ui.profile.ProfileFragment;

import java.util.Objects;

import static com.remembrall.ui.groceryList.GroceryListFragment.LAUNCH_CREATE_GROCERY_LIST;
import static com.remembrall.ui.groceryListEntry.GroceryListEntryFragment.LAUNCH_CREATE_GROCERY_LIST_ENTRY;

public class MainActivity extends AppCompatActivity
        implements GroceryListFragment.GroceryListListener, LoginFragment.LoginListener,
                   RegistrationFragment.RegistrationListener, LoginRequiredListener,
                   FailedListener {

    /* Define the names of different Intent which this activity accepts*/
    public static final String INTENT_NAME = "action";

    private AlphaAnimation failAnimation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();
        setupFailedChip();
        setupListeners();
        setupNavigation();

        if (!ServiceLocator.getInstance().get(Backend.class).isSessionAvailable()) {
            // TODO: check for invitations intent from push notification so that the user is forwarded after login
            showLoginRegistration();
            return;
        }

        loginFulfilled();

        if (Objects.equals(getIntent().getAction(), "OPEN_INVITATIONS")) {
            showInvitationsAndFocus();
            return;
        }

        if (Objects.equals(getIntent().getAction(), "OPEN_LIST")) {
            showGroceryListEntries(Long.parseLong((String) getIntent().getExtras().get("listId")));
            return;
        }

        if (findViewById(R.id.main_fragment) != null) {
            if (savedInstanceState != null) {
                return;
            }

            showGroceryLists(false);
        }
    }

    private void setupToolbar() {
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
    }

    private void setupListeners() {
        ServiceLocator.getInstance()
                      .get(NetworkResponseHandler.class)
                      .register((LoginRequiredListener) this);
        ServiceLocator.getInstance()
                      .get(NetworkResponseHandler.class)
                      .register((FailedListener) this);
    }

    private void loginFulfilled() {
        findViewById(R.id.navigation).setVisibility(View.VISIBLE);
        findViewById(R.id.floating_plus_button).setVisibility(View.VISIBLE);
    }

    private void setupFailedChip() {
        View view = findViewById(R.id.failed_chip);

        failAnimation = new AlphaAnimation(1f, 0f);
        failAnimation.setStartOffset(0);
        failAnimation.setDuration(3000);
        failAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // nothing to do
            }
        });
    }

    private void setupNavigation() {
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
                case R.id.action_profile:
                    showProfile();
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
                                   .commitAllowingStateLoss();
        findViewById(R.id.floating_plus_button).setVisibility(View.INVISIBLE);
    }

    private void showInvitationsAndFocus() {
        showInvitations();
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.action_invitations);
    }

    private void showProfile() {
        ProfileFragment fragment = new ProfileFragment();
        getSupportFragmentManager().beginTransaction()
                                   .replace(R.id.main_fragment, fragment)
                                   .addToBackStack("profile")
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
        super.onNewIntent(intent);

        if (intent.getExtras() != null) {
            if (Objects.equals(intent.getExtras().get(INTENT_NAME), "OPEN_INVITATIONS")) {
                showInvitationsAndFocus();
            } else if (Objects.equals(intent.getExtras().get(INTENT_NAME), "OPEN_LIST")) {
                showGroceryListEntries(Long.parseLong((String) intent.getExtras().get("listId")));
            }
        }
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

    @Override
    public void onFail(String message) {
        Chip view = findViewById(R.id.failed_chip);
        view.setText(message);
        view.startAnimation(failAnimation);
    }
}
