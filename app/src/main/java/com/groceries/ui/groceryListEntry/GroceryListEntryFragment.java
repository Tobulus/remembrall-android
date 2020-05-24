package com.groceries.ui.groceryListEntry;

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
import com.groceries.R;
import com.groceries.model.database.GroceryListEntry;
import com.groceries.model.view.GroceryListEntryModel;
import com.groceries.model.view.factory.GroceryListEntryModelFactory;
import com.groceries.ui.activity.CreateGroceryListEntryActivity;
import com.groceries.ui.activity.CreateInvitationActivity;

public class GroceryListEntryFragment extends Fragment {

    private GroceryListEntryListener mListener;
    private Long groceryListId;

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
        GroceryListEntryViewAdapter adapter =
                new GroceryListEntryViewAdapter(this, model, mListener);
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
            Intent intent = new Intent(getContext(), CreateGroceryListEntryActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong("id", groceryListId);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof GroceryListEntryListener) {
            mListener = (GroceryListEntryListener) context;
        } else {
            throw new RuntimeException(context.toString()
                                       + " must implement GroceryListEntryListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface GroceryListEntryListener {
        void onClick(GroceryListEntry entry);
    }
}
