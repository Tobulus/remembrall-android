package com.groceries.ui.groceryList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.groceries.model.database.GroceryList;
import com.groceries.model.view.GroceryListModel;
import com.groceries.ui.activity.CreateGroceryListActivity;

public class GroceryListFragment extends Fragment {

    private static final int LAUNCH_CREATE_GROCERY_LIST = 1;

    private GroceryListListener mListener;
    private GroceryListViewAdapter adapter;

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
        GroceryListModel model = ViewModelProviders.of(this).get(GroceryListModel.class);

        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new GroceryListViewAdapter(this, model, mListener);
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
            Intent intent = new Intent(getContext(), CreateGroceryListActivity.class);
            startActivityForResult(intent, LAUNCH_CREATE_GROCERY_LIST);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_CREATE_GROCERY_LIST && resultCode == Activity.RESULT_OK) {
            adapter.refresh();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof GroceryListListener) {
            mListener = (GroceryListListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement GroceryListListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface GroceryListListener {
        void onClick(GroceryList item);
    }
}
