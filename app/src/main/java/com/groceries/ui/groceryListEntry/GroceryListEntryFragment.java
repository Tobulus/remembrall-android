package com.groceries.ui.groceryListEntry;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.groceries.R;
import com.groceries.api.BackendProvider;
import com.groceries.model.GroceryListEntry;
import com.groceries.ui.activity.CreateGroceryListEntryActivity;

import java.util.ArrayList;
import java.util.List;

public class GroceryListEntryFragment extends Fragment {

    private GroceryListEntryListener mListener;
    private BackendProvider backendProvider;
    private Long groceryListId;

    public GroceryListEntryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            groceryListId = getArguments().getLong("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grocery_list_entries, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            List<GroceryListEntry> entries = new ArrayList<>();
            GroceryListEntryViewAdapter adapter =
                    new GroceryListEntryViewAdapter(entries, mListener, backendProvider);
            recyclerView.setAdapter(adapter);
            backendProvider.getBackend().getGroceryListEntries(groceryListId, e -> {
                entries.addAll(e);
                adapter.notifyDataSetChanged();
            }, error -> {
            });
        }

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

        if (context instanceof BackendProvider) {
            backendProvider = (BackendProvider) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement BackendProvider");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        backendProvider = null;
    }

    public interface GroceryListEntryListener {
        void onClick(GroceryListEntry entry);
    }
}
