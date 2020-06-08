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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.remembrall.R;
import com.remembrall.model.database.GroceryListEntry;
import com.remembrall.model.view.GroceryListEntryModel;
import com.remembrall.model.view.factory.GroceryListEntryModelFactory;
import com.remembrall.ui.activity.CreateInvitationActivity;

public class GroceryListEntryFragment extends Fragment {

    static final int LAUNCH_CREATE_GROCERY_LIST_ENTRY = 1;

    private Long groceryListId;
    private GroceryListEntryViewAdapter adapter;

    public GroceryListEntryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            groceryListId = getArguments().getLong("id");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.invitation, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_invitation) {
            Intent intent = new Intent(getContext(), CreateInvitationActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong("id", groceryListId);
            intent.putExtras(bundle);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grocery_list_entries, container, false);
        GroceryListEntryModel model = ViewModelProviders.of(this,
                                                            new GroceryListEntryModelFactory(
                                                                    groceryListId,
                                                                    requireActivity().getApplication()))
                                                        .get(GroceryListEntryModel.class);

        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new GroceryListEntryViewAdapter(this, model);
        recyclerView.setAdapter(adapter);

        ((SwipeRefreshLayout) view).setOnRefreshListener(() -> {
            adapter.refresh();
            ((SwipeRefreshLayout) view).setRefreshing(false);
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        FloatingActionButton fab = getActivity().findViewById(R.id.floating_plus_button);
        fab.setOnClickListener(v -> {
            GroceryListEntryDialog dialog = new GroceryListEntryDialog();
            Bundle bundle = new Bundle();
            bundle.putLong("id", groceryListId);
            dialog.setArguments(bundle);
            dialog.setTargetFragment(this, LAUNCH_CREATE_GROCERY_LIST_ENTRY);
            dialog.show(requireFragmentManager(), "create-grocery-list-entry");
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_CREATE_GROCERY_LIST_ENTRY && resultCode == Activity.RESULT_OK) {
            adapter.refresh();
        }
    }

    public interface GroceryListEntryListener {
        void onClick(GroceryListEntry entry);
    }
}
