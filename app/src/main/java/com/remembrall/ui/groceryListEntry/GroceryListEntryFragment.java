package com.remembrall.ui.groceryListEntry;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.remembrall.R;
import com.remembrall.api.Backend;
import com.remembrall.listener.BackPressedListener;
import com.remembrall.locator.ServiceLocator;
import com.remembrall.model.database.GroceryListEntry;
import com.remembrall.model.view.GroceryListEntryModel;
import com.remembrall.model.view.factory.GroceryListEntryModelFactory;
import com.remembrall.ui.invitation.InvitationDialog;

import java.security.InvalidParameterException;

public class GroceryListEntryFragment extends Fragment implements BackPressedListener {

    public static final int LAUNCH_CREATE_GROCERY_LIST_ENTRY = 1;
    public static final int LAUNCH_CREATE_INVITATION = 2;

    private Long groceryListId;
    private GroceryListEntryViewAdapter adapter;
    private SwipeRefreshLayout swipe;

    public GroceryListEntryFragment() {
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong("id", groceryListId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            groceryListId = getArguments().getLong("id");
        }

        if (savedInstanceState != null) {
            groceryListId = savedInstanceState.getLong("id");
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_grocery_list_entry, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_edit).setEnabled(adapter.getNofSelectedItems() == 1);
        menu.findItem(R.id.action_edit).setVisible(adapter.getNofSelectedItems() == 1);

        menu.findItem(R.id.action_delete).setEnabled(adapter.getNofSelectedItems() > 0);
        menu.findItem(R.id.action_delete).setVisible(adapter.getNofSelectedItems() > 0);

        menu.findItem(R.id.action_invitation).setEnabled(adapter.getNofSelectedItems() == 0);
        menu.findItem(R.id.action_invitation).setVisible(adapter.getNofSelectedItems() == 0);

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_invitation) {
            showInvitationDialog();
            return true;
        }

        if (item.getItemId() == R.id.action_edit) {
            showGroceryListEntryDialog();
            return true;
        }

        if (item.getItemId() == R.id.action_delete) {
            deleteSelectedItems();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grocery_list_entries, container, false);
        swipe = (SwipeRefreshLayout) view;
        GroceryListEntryModel model = ViewModelProviders.of(this,
                                                            new GroceryListEntryModelFactory(
                                                                    groceryListId,
                                                                    requireActivity().getApplication()))
                                                        .get(GroceryListEntryModel.class);

        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new GroceryListEntryViewAdapter(this, model, recyclerView);
        recyclerView.setAdapter(adapter);

        swipe.setOnRefreshListener(() -> {
            adapter.refresh();
            swipe.setRefreshing(false);
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == LAUNCH_CREATE_GROCERY_LIST_ENTRY) && resultCode == Activity.RESULT_OK) {
            swipe.post(() -> {
                swipe.setRefreshing(true);
                adapter.refresh();
                swipe.setRefreshing(false);
            });
        }

        if (requestCode == LAUNCH_CREATE_INVITATION && resultCode == Activity.RESULT_OK) {
            Toast.makeText(getContext(), "Invitation has been created", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onBackPressed() {
        return adapter.unselect();
    }

    private void showInvitationDialog() {
        InvitationDialog dialog = new InvitationDialog();
        Bundle bundle = new Bundle();
        bundle.putLong("id", groceryListId);
        dialog.setTargetFragment(this, LAUNCH_CREATE_INVITATION);
        dialog.setArguments(bundle);
        dialog.show(getParentFragmentManager(), "create-invitation");
    }

    private void showGroceryListEntryDialog() {
        Bundle bundle = new Bundle();
        bundle.putLong("id", groceryListId);
        GroceryListEntryDialog dialog =
                new GroceryListEntryDialog(adapter.getSelectedGroceryListEntry()
                                                  .orElseThrow(() -> new InvalidParameterException(
                                                          "Entry selection is empty")));
        dialog.setTargetFragment(this, LAUNCH_CREATE_GROCERY_LIST_ENTRY);
        dialog.setArguments(bundle);
        dialog.show(getParentFragmentManager(), "edit-grocery-list-entry");
    }

    private void deleteSelectedItems() {
        adapter.getSelectedGroceryListEntries()
               .forEach(entry -> ServiceLocator.getInstance()
                                               .get(Backend.class)
                                               .deleteGroceryListEntry(groceryListId,
                                                                       entry,
                                                                       json -> adapter.refresh(),
                                                                       error -> Toast.makeText(
                                                                               getContext(),
                                                                               error.getMessage(),
                                                                               Toast.LENGTH_LONG)
                                                                                     .show()));
    }

    public interface GroceryListEntryListener {
        void onClick(GroceryListEntry entry);
    }
}
