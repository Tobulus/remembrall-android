package com.groceries.ui.groceryList;

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
import com.groceries.model.GroceryList;
import com.groceries.ui.activity.CreateGroceryListActivity;

import java.util.ArrayList;
import java.util.List;

public class GroceryListFragment extends Fragment {

    private GroceryListListener mListener;
    private BackendProvider backendProvider;

    public GroceryListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grocery_lists, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            List<GroceryList> lists = new ArrayList<>();
            GroceryListViewAdapter adapter = new GroceryListViewAdapter(lists, mListener);
            recyclerView.setAdapter(adapter);
            backendProvider.getBackend().getGroceryLists(l -> {
                lists.addAll(l);
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
            Intent intent = new Intent(getContext(), CreateGroceryListActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof GroceryListListener) {
            mListener = (GroceryListListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement GroceryListListener");
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

    public interface GroceryListListener {
        void onClick(GroceryList item);
    }
}
